import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	ArrayList<Integer> connectedClients = new ArrayList<>();
	TheServer server;
	private Consumer<Serializable> callback;

	Server(Consumer<Serializable> call) {
		synchronized (this) {
			callback = call;
			server = new TheServer();
			server.start();
		}
	}

	public class TheServer extends Thread {

		public void run() {

			try (ServerSocket mysocket = new ServerSocket(5555);) {
				System.out.println("Server is waiting for a client!");

				while (true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);

					clients.add(c);
					connectedClients.clear();
					for (ClientThread i : clients) {
						connectedClients.add(i.count);
					}
					System.out.println("Clients size is " + clients.size());
					callback.accept("Client #" + count + " has connected to server.");

					c.start();

					count++;

				}
			} // end of try
			catch (Exception e) {
				callback.accept("Server socket did not launch");
			}
		}// end of while
	}

	class ClientThread extends Thread {

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		ClientThread(Socket s, int count) {
			this.connection = s;
			this.count = count;
		}

		// function to print message for clients
		public synchronized void updateClients(String message) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					t.out.writeObject("Client #" + count + " said: " + message + "-" + connectedClients.toString());
				} catch (Exception e) {
				}
			}
		}

		public void updateClientStatus(String message, ArrayList<ClientThread> clients, boolean isOnline) {
			for (ClientThread i : clients) {
				try {
					if (!isOnline) { // false
						i.out.writeObject("Client #" + count + " left-" + connectedClients.toString());
					} else {
						i.out.writeObject(message + "-" + connectedClients.toString());
					}
				} catch (Exception e) {
				}
			}
		}

		public void updateClientMessage(String message, MessageData receivedData, int from) throws IOException {
			int currClient = 0;
			int otherClient = 0;
			int i = 0;
			while (true) {
				if (clients.get(i).count == from) {
					clients.get(i).out.writeObject("Client #" + from + " said: " + message + "-" + connectedClients.toString());
					break;
				}
				i++;
			}

			while (currClient < receivedData.clientID.size()) {
				if (clients.get(otherClient).count != receivedData.clientID.get(currClient)) {
					otherClient++;
				}
				if (clients.get(otherClient).count == receivedData.clientID.get(currClient)) {
					ClientThread t = clients.get(otherClient);
					// send message to other client (1 or group)
					t.out.writeObject("Client #" + from + " said: " + message + "-" + connectedClients.toString());
					currClient++;
				}
			}
		}

		public synchronized void run() {

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch (Exception e) {
				System.out.println("Streams not open");
			}

			// updateClients("new client on server: client #"+count);
			updateClientStatus("Client #" + count + " is on server", clients, true);

			while (true) {
				try {
					MessageData data = (MessageData) in.readObject();
					callback.accept("Client #" + count + " sent: " + data.getText());
					if (data.isToAll()) {
						updateClients(data.getText() + "-" + connectedClients.toString());
					} else {
						updateClientMessage(data.getText(), data, count);
					}

				} catch (Exception e) {
					callback.accept("Client #" + count + " has left the server!");

					updateClientStatus("Client #" + count + " has left the server!", clients, false);
					clients.remove(this);
					for (int i = 0; i < connectedClients.size(); i++) {
						if (connectedClients.get(i) == count) {
							connectedClients.remove(i);
						}
					}
					//updateClients("client #" + count + "has left the server!" + "-" + connectedClients.toString());
					break;
				}
			}
		}// end of run

		public void send(ArrayList<ClientThread> data) {
			try {
				out.writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}// end of client thread
}
