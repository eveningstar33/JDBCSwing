package jdbc.employeesearch.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import jdbc.employeesearch.core.AuditHistory;
import jdbc.employeesearch.core.Employee;
import jdbc.employeesearch.core.User;
import jdbc.employeesearch.dao.EmployeeDAO;
import jdbc.employeesearch.dao.UserDAO;
import jdbc.employeesearch.gui.audithistory.AuditHistoryDialog;
import jdbc.employeesearch.gui.employees.EmployeeDialog;
import jdbc.employeesearch.gui.employees.EmployeeTableModel;
import jdbc.employeesearch.gui.users.ChangePasswordDialog;
import jdbc.employeesearch.gui.users.UserDialog;
import jdbc.employeesearch.gui.users.UserLoginDialog;
import jdbc.employeesearch.gui.users.UserTableModel;

public class EmployeeSearchApp extends JFrame {
		
	private JPanel contentPane;
	private JTextField lastNameTextField;
	private JButton btnSearch;
	private JScrollPane scrollPane;
	private JPanel panel_1;
	private JButton btnAddEmployee;
	private JButton btnUpdateEmployee;
	private JButton viewHistoryButton;
	
	private int userId;
	private JPanel topPanel;
	private JLabel lblLoggedIn;
	private JLabel loggedInUserLabel;
	private JPanel searchPanel;
	private JTabbedPane tabbedPane;
	private JPanel employeePanel;
	private JTable employeeTable;
	private JPanel employeesButtonPanel;
	private JPanel usersPanel;
	private JScrollPane userScrollPane;
	private JTable userTable;
	private JPanel panel;
	private JButton addUserButton;
	private JButton updateUser;
	private JButton changePasswordButton;
	
	private EmployeeDAO employeeDAO;
	private UserDAO userDAO;
	
	private boolean admin;
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {
					
					EmployeeDAO employeeDAO = new EmployeeDAO();
					UserDAO userDAO = new UserDAO();

					// Get users
					List<User> users = userDAO.getUsers(true, 0);
					
					// Show login dialog
					UserLoginDialog dialog = new UserLoginDialog();
					dialog.populateUsers(users);
					dialog.setEmployeeDAO(employeeDAO);
					dialog.setUserDAO(userDAO);
					
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception exc) {

					System.out.println(exc);
				}
			}
		});
	}

	
	/**
	 * Create the frame.
	 */
	
	public EmployeeSearchApp(int theUserId, boolean theAdmin, 
			EmployeeDAO theEmployeeDAO, UserDAO theUserDAO) {		
			
			userId = theUserId;
			admin = theAdmin;
			
			employeeDAO = theEmployeeDAO;
			userDAO = theUserDAO;

			setTitle("Employee Search App");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 735, 457);			
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPane.add(tabbedPane, BorderLayout.CENTER);

			employeePanel = new JPanel();
			tabbedPane.addTab("Employees", null, employeePanel, null);
			employeePanel.setLayout(new BorderLayout(0, 0));

			panel_1 = new JPanel();
			employeePanel.add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(new BorderLayout(0, 0));
			
			employeeTable = new JTable();
			panel_1.add(employeeTable.getTableHeader(), BorderLayout.NORTH);
			panel_1.add(employeeTable, BorderLayout.CENTER);
			
			searchPanel = new JPanel();
			employeePanel.add(searchPanel, BorderLayout.NORTH);
			searchPanel.setBorder(null);
			FlowLayout flowLayout_1 = (FlowLayout) searchPanel.getLayout();
			flowLayout_1.setAlignment(FlowLayout.LEFT);
			
			JLabel lblEnterLastName = new JLabel("Enter last name");
			searchPanel.add(lblEnterLastName);
			
			lastNameTextField = new JTextField();
			searchPanel.add(lastNameTextField);
			lastNameTextField.setColumns(10);
			
			btnSearch = new JButton("Search");
			searchPanel.add(btnSearch);
		
			btnSearch.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
				
					// Get last name from the text field

					// Call DAO and get employees for the last name

					// If last name is empty, then get all employees

					// Print out employees				
				
					try {
				
						String lastName = lastNameTextField.getText();
					
						List<Employee> employees = null;
						
						if (lastName != null && lastName.trim().length() > 0) 
								employees = employeeDAO.searchEmployees(lastName);

						else 
							employees = employeeDAO.getAllEmployees();
					
						// create the model and update the "table"
						EmployeeTableModel model = new EmployeeTableModel(employees);
						employeeTable.setModel(model);
					
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(EmployeeSearchApp.this, "Error: " + exc, 
								"Error", JOptionPane.ERROR_MESSAGE); 
					}
				}
			});
			
			scrollPane = new JScrollPane();
			
			employeesButtonPanel = new JPanel();
			employeePanel.add(employeesButtonPanel, BorderLayout.SOUTH);

			
		
		    btnAddEmployee = new JButton("Add Employee");
		    btnAddEmployee.addActionListener(new ActionListener() {
			
		    	public void actionPerformed(ActionEvent e) {
				
		    		// create dialog
		    		EmployeeDialog dialog = new EmployeeDialog(EmployeeSearchApp.this, employeeDAO, userId);
				
		    		// show dialog
		    		dialog.setVisible(true);

		    	}
		    });
		    
			employeesButtonPanel.add(btnAddEmployee);
		    		
			btnUpdateEmployee = new JButton("Update Employee");
			btnUpdateEmployee.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
				
					// get the selected item
					int row = employeeTable.getSelectedRow();
				
					// make sure a row is selected
					if (row < 0) {
					
						JOptionPane.showMessageDialog(EmployeeSearchApp.this, "You must select an employee", 
								"Error", JOptionPane.ERROR_MESSAGE);				
						return;
					}
				
					// get the current employee
					Employee tempEmployee = (Employee) employeeTable.getValueAt(row, 
							EmployeeTableModel.OBJECT_COL);
								
					// create dialog
					EmployeeDialog dialog = new EmployeeDialog(EmployeeSearchApp.this, employeeDAO, 
															tempEmployee, true, userId);

					// show dialog
					dialog.setVisible(true);
				}
			});
			
			employeesButtonPanel.add(btnUpdateEmployee);

			
			viewHistoryButton = new JButton("View History");
			viewHistoryButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					// get the selected item
					int row = employeeTable.getSelectedRow();
					
					// make sure a row is selected
					if (row < 0) {
						JOptionPane.showMessageDialog(EmployeeSearchApp.this, "You must select an employee", 
								"Error", JOptionPane.ERROR_MESSAGE);				
						return;
					}
					
					try {
						
						// get the current employee
						Employee tempEmployee = (Employee) employeeTable.getValueAt(row, 
								EmployeeTableModel.OBJECT_COL);

						// get audit history for this employee
						int employeeId = tempEmployee.getId();
						List<AuditHistory> auditHistoryList = employeeDAO.getAuditHistory(employeeId);

						// show audit history dialog
						AuditHistoryDialog dialog = new AuditHistoryDialog();
						dialog.populate(tempEmployee, auditHistoryList);
						
						dialog.setVisible(true);
					} catch (Exception exc) {
						
						exc.printStackTrace();
						JOptionPane.showMessageDialog(EmployeeSearchApp.this, "Error retrieving audit history", 
								"Error", JOptionPane.ERROR_MESSAGE);				
						return;					
					}
				}
			});

			employeesButtonPanel.add(viewHistoryButton);
			
			usersPanel = new JPanel();
			tabbedPane.addTab("Users", null, usersPanel, null);
			usersPanel.setLayout(new BorderLayout(0, 0));
			
			userScrollPane = new JScrollPane();
			usersPanel.add(userScrollPane, BorderLayout.CENTER);
			
			userTable = new JTable();
			
			usersPanel.add(userTable.getTableHeader(), BorderLayout.NORTH);
			usersPanel.add(userTable, BorderLayout.CENTER);
			
			panel = new JPanel();
			usersPanel.add(panel, BorderLayout.SOUTH);

			addUserButton = new JButton("Add User");
			addUserButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					addUser();
				}
			});
			panel.add(addUserButton);

			updateUser = new JButton("Update User");
			updateUser.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					updateUser();
				}
			});
			panel.add(updateUser);
			
			changePasswordButton = new JButton("Change Password");
			changePasswordButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					changePassword();
				}
			});
			panel.add(changePasswordButton);

			topPanel = new JPanel();
			contentPane.add(topPanel, BorderLayout.NORTH);
			FlowLayout flowLayout_2 = (FlowLayout) topPanel.getLayout();
			flowLayout_2.setAlignment(FlowLayout.LEFT);
			
			lblLoggedIn = new JLabel("Logged In:");
			topPanel.add(lblLoggedIn);
			
			loggedInUserLabel = new JLabel("New label");
			topPanel.add(loggedInUserLabel);

			addUserButton.setEnabled(admin);
			
			refreshUsersView();
	}
	
	public void refreshUsersView() {

		try {
			
			List<User> users = userDAO.getUsers(admin, userId);

			// create the model and update the "table"
			UserTableModel model = new UserTableModel(users);

			userTable.setModel(model);
		} catch (Exception exc) {
			
			JOptionPane.showMessageDialog(this, "Error: " + exc, "Error",
					JOptionPane.ERROR_MESSAGE);
		}	
	}

	
	public void refreshEmployeesView() {
		
		try {
			
			List<Employee> employees = employeeDAO.getAllEmployees();
			
			// create the model and update the "table"
			EmployeeTableModel model = new EmployeeTableModel(employees);

			employeeTable.setModel(model);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "Error: " + e, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	} 
	
	public void setLoggedInUserName(String firstName, String lastName) {
		
		loggedInUserLabel.setText(firstName + " " + lastName);
	}
	
	private void addUser() {
		
		UserDialog userDialog = new UserDialog(EmployeeSearchApp.this, userDAO, false);
		
		userDialog.setVisible(true);
	}

	protected void updateUser() {

		// get the selected item
		int row = userTable.getSelectedRow();
		
		// make sure a row is selected
		if (row < 0) {
			
			JOptionPane.showMessageDialog(EmployeeSearchApp.this, "You must select a user", "Error",
					JOptionPane.ERROR_MESSAGE);				
			return;
		}
		
		// get the current employee
		User tempUser = (User) userTable.getValueAt(row, UserTableModel.OBJECT_COL);
		
		// create dialog
		UserDialog dialog = new UserDialog(EmployeeSearchApp.this, userDAO, tempUser);

		// show dialog
		dialog.setVisible(true);		
	}
	
	protected void changePassword() {
		
		// get the selected item
		int row = userTable.getSelectedRow();
		
		// make sure a row is selected
		if (row < 0) {
			
			JOptionPane.showMessageDialog(EmployeeSearchApp.this, "You must select a user", "Error",
					JOptionPane.ERROR_MESSAGE);				
			return;
		}
		
		// get the current employee
		User tempUser = (User) userTable.getValueAt(row, UserTableModel.OBJECT_COL);

		// create dialog
		ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(tempUser, userDAO);
		
		// show dialog
		changePasswordDialog.setVisible(true);
	}

	
	public int getCurrentUserId() {
		
		return userId;
	}

	public void setAdmin(boolean theFlag) {
		
		admin = theFlag;
		addUserButton.setEnabled(theFlag);		
	}
}
