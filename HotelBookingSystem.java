package hotelBookingSystem;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;

public class HotelBookingSystem {
	
	//Step 2 : Add the URL, user and password...
	private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
	private static final String user = "root";
	private static final String password = "aditya";
	//End
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
		// TODO Auto-generated method stub
		
		//Step 1 : Load the Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//End
		
		//Step 3 : Establish a connection.
		try {
			Connection con = DriverManager.getConnection(url, user, password);
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			
			while(true) {
				System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                
                int option = sc.nextInt();
                switch(option) {
                case 1:
                    reserveRoom(con, sc, stmt);
                    break;
                case 2:
                    viewReservations(con, stmt);
                    break;
                case 3:
                    getRoomNumber(con, sc, stmt);
                    break;
                case 4:
                    updateReservation(con, sc, stmt);
                    break;
                case 5:
                    deleteReservation(con, sc, stmt,rs);
                    break;
                case 0:
                    exit();
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
                }
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		//End
	}
	//method 1
	private static void reserveRoom(Connection con, Scanner sc, Statement stmt) {
		System.out.println("Enter the guest name : ");
		String guest_name = sc.next();
		
		System.out.println("Enter the room number : ");
		int room_number = sc.nextInt();
		
		System.out.println("Enter the contact number : ");
		int contact_number = sc.nextInt();
		
		//String query = "INSERT INTO reservations(guest_name, room_number, contact_number)"+"Values ('"+guest_name+"'+"+room_number+"+'"+contact_number+"')";
		
		String query = "INSERT INTO reservations (guest_name, room_number, contact_number) " + "VALUES ('" + guest_name + "', " + room_number + ", '" + contact_number + "')";
		
		try {
			int noOfRowsAffected = stmt.executeUpdate(query);
			
			if(noOfRowsAffected > 0) {
				System.out.println("Room is reserved.");
			}
			else {
				System.out.println("Reservation failed.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	
	//method 2
	private static void viewReservations(Connection con, Statement stmt) {
		String query = "SELECT guest_name, room_number, contact_number FROM reservations;";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String guest_name = rs.getString("guest_name");
				int room_number = rs.getInt("room_number");
				int contact_number = rs.getInt("contact_number");
				
				System.out.println("Guest Name : "+guest_name);
				System.out.println("Room Number : "+room_number);
				System.out.println("Contact Number :"+contact_number);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	
	//method 3
	private static void getRoomNumber(Connection con, Scanner sc, Statement stmt) {
		System.out.println("Enter the Reservation ID : ");
		int reservtion_id = sc.nextInt();
		
		System.out.println("Enter the Guest's Name : ");
		String guest_name = sc.next();
		
		//String query = "SELECT room_number FROM reservations "+" WHERE reservation_id = "+reservtion_id+"AND guest_name = '"+guest_name+"'";
		
		String query = "SELECT room_number FROM reservations " + "WHERE reservation_id = " + reservtion_id + " AND guest_name = '" + guest_name + "'";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				String room_number = rs.getString("room_number");
				System.out.println("Room Number is : "+room_number);
			}
			else {
				System.out.println("Reservation is not found for the id and name.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	
	//method 4
	private static void updateReservation(Connection con, Scanner sc, Statement stmt) {
		try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            if (!reservationExists(con, sc, stmt, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try  {
                int affectedRows = stmt.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
            finally {
            	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	//End
	
	//method 5
	private static void deleteReservation(Connection con, Scanner sc, Statement stmt, ResultSet rs) {
		try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(con,sc,stmt, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		}
	//End
	
	//method 6
	public static void exit() throws InterruptedException {
		System.out.print("Exiting System");
		int i = 5;
		while(i!=0) {
			System.out.print(".");
            Thread.sleep(1000);
            i--;
		}
		System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
	}
	//End
	
	//method 7
	private static boolean reservationExists(Connection con, Scanner sc, Statement stmt, int reservation_id) {
		String query = "SELECT * FROM reservations WHERE reservation_id = "+ reservation_id;
		try(ResultSet rs = stmt.executeQuery(query)) {
			return rs.next();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	//End
}
