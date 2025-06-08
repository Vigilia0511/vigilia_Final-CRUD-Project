//vigilia, marc joshua c. BSIT 3A
package librarymanagement;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class LibraryManagementSystem extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change if your MySQL has a password
    
    // Main components
    private JTabbedPane tabbedPane;
    
    // Books tab components
    private JTextField tfBookId, tfBookTitle, tfBookAuthor, tfBookGenre, tfBookQuantity;
    private JTable booksTable;
    private DefaultTableModel booksModel;
    private JTextField tfSearchBooks;
    
    // Members tab components
    private JTextField tfMemberId, tfMemberName, tfMemberEmail, tfMemberPhone;
    private JTable membersTable;
    private DefaultTableModel membersModel;
    private JTextField tfSearchMembers;
    
    // Borrowings tab components
    private JTextField tfBorrowingId;
    private JTextField tfBorrowDate, tfReturnDate;
    private JTable borrowingsTable;
    private DefaultTableModel borrowingsModel;
    private JTextField tfSearchBorrowings;
    private JComboBox<String> cbMemberSelect, cbBookSelect;
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        EventQueue.invokeLater(() -> {
            try {
                LibraryManagementSystem frame = new LibraryManagementSystem();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LibraryManagementSystem() {
        initializeGUI();
        loadAllData();
    }

    private void initializeGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 800);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        createBooksTab();
        createMembersTab();
        createBorrowingsTab();
        createReportsTab();
        
        getContentPane().add(tabbedPane);
    }

    private void createBooksTab() {
        JPanel booksPanel = new JPanel(null);
        
        // Search section
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBorder(new TitledBorder("Search Books"));
        searchPanel.setBounds(20, 20, 1140, 80);
        
        JLabel lblSearchBooks = new JLabel("Search by ID, Title, Author, or Genre:");
        lblSearchBooks.setBounds(20, 30, 250, 25);
        lblSearchBooks.setFont(new Font("Dialog", Font.BOLD, 12));
        searchPanel.add(lblSearchBooks);
        
        tfSearchBooks = new JTextField();
        tfSearchBooks.setBounds(280, 30, 200, 25);
        searchPanel.add(tfSearchBooks);
        
        JButton btnSearchBooks = new JButton("Search");
        btnSearchBooks.setBounds(500, 30, 80, 25);
        searchPanel.add(btnSearchBooks);
        
        JButton btnShowAllBooks = new JButton("Show All");
        btnShowAllBooks.setBounds(600, 30, 100, 25);
        searchPanel.add(btnShowAllBooks);
        
        JButton btnClearSearchBooks = new JButton("Clear");
        btnClearSearchBooks.setBounds(720, 30, 80, 25);
        searchPanel.add(btnClearSearchBooks);
        
        booksPanel.add(searchPanel);
        
        // Book form section
        JPanel bookFormPanel = new JPanel(null);
        bookFormPanel.setBorder(new TitledBorder("Book Information"));
        bookFormPanel.setBounds(20, 120, 600, 220);
        
        // Book ID
        JLabel lblBookId = new JLabel("Book ID:");
        lblBookId.setBounds(30, 40, 100, 25);
        lblBookId.setFont(new Font("Dialog", Font.BOLD, 12));
        bookFormPanel.add(lblBookId);
        
        tfBookId = new JTextField();
        tfBookId.setBounds(140, 40, 150, 25);
        tfBookId.setFont(new Font("Dialog", Font.PLAIN, 12));
        bookFormPanel.add(tfBookId);
        
        // Title
        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setBounds(30, 80, 100, 25);
        lblTitle.setFont(new Font("Dialog", Font.BOLD, 12));
        bookFormPanel.add(lblTitle);
        
        tfBookTitle = new JTextField();
        tfBookTitle.setBounds(140, 80, 150, 25);
        tfBookTitle.setFont(new Font("Dialog", Font.PLAIN, 12));
        bookFormPanel.add(tfBookTitle);
        
        // Author
        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setBounds(30, 120, 100, 25);
        lblAuthor.setFont(new Font("Dialog", Font.BOLD, 12));
        bookFormPanel.add(lblAuthor);
        
        tfBookAuthor = new JTextField();
        tfBookAuthor.setBounds(140, 120, 150, 25);
        tfBookAuthor.setFont(new Font("Dialog", Font.PLAIN, 12));
        bookFormPanel.add(tfBookAuthor);
        
        // Genre
        JLabel lblGenre = new JLabel("Genre:");
        lblGenre.setBounds(30, 160, 100, 25);
        lblGenre.setFont(new Font("Dialog", Font.BOLD, 12));
        bookFormPanel.add(lblGenre);
        
        tfBookGenre = new JTextField();
        tfBookGenre.setBounds(140, 160, 150, 25);
        tfBookGenre.setFont(new Font("Dialog", Font.PLAIN, 12));
        bookFormPanel.add(tfBookGenre);
        
        // Quantity
        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(350, 40, 100, 25);
        lblQuantity.setFont(new Font("Dialog", Font.BOLD, 12));
        bookFormPanel.add(lblQuantity);
        
        tfBookQuantity = new JTextField();
        tfBookQuantity.setBounds(460, 40, 100, 25);
        tfBookQuantity.setFont(new Font("Dialog", Font.PLAIN, 12));
        bookFormPanel.add(tfBookQuantity);
        
        booksPanel.add(bookFormPanel);
        
        // Book actions section
        JPanel bookActionsPanel = new JPanel(null);
        bookActionsPanel.setBorder(new TitledBorder("Actions"));
        bookActionsPanel.setBounds(640, 120, 220, 220);
        
        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.setBounds(20, 40, 180, 30);
        bookActionsPanel.add(btnAddBook);
        
        JButton btnUpdateBook = new JButton("Update Book");
        btnUpdateBook.setBounds(20, 80, 180, 30);
        bookActionsPanel.add(btnUpdateBook);
        
        JButton btnDeleteBook = new JButton("Delete Book");
        btnDeleteBook.setBounds(20, 120, 180, 30);
        bookActionsPanel.add(btnDeleteBook);
        
        JButton btnClearBookFields = new JButton("Clear Fields");
        btnClearBookFields.setBounds(20, 160, 180, 30);
        bookActionsPanel.add(btnClearBookFields);
        
        booksPanel.add(bookActionsPanel);
        
        // Books table - Create DefaultTableModel first, then JTable
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Total Copies", "Available"};
        
        // Create the DefaultTableModel first
        booksModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        // Create table with the DefaultTableModel
        booksTable = new JTable(booksModel);
        
        // Set table properties
        booksTable.setFont(new Font("Dialog", Font.PLAIN, 12));
        booksTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        booksTable.setRowHeight(25);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setReorderingAllowed(false);
        
        // Create scroll pane
        JScrollPane booksScrollPane = new JScrollPane();
        booksScrollPane.setBounds(20, 360, 1140, 350);
        booksScrollPane.setViewportView(booksTable);
        booksScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        booksScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Add to main panel
        booksPanel.add(booksScrollPane);
        
        // Event listeners
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                populateBookFields();
            }
        });
        
        tfSearchBooks.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchBooks(tfSearchBooks.getText().trim());
            }
        });
        
        btnAddBook.addActionListener(e -> addBook());
        btnUpdateBook.addActionListener(e -> updateBook());
        btnDeleteBook.addActionListener(e -> deleteBook());
        btnClearBookFields.addActionListener(e -> clearBookFields());
        btnSearchBooks.addActionListener(e -> searchBooks(tfSearchBooks.getText().trim()));
        btnShowAllBooks.addActionListener(e -> { tfSearchBooks.setText(""); loadBooksData(); });
        btnClearSearchBooks.addActionListener(e -> { tfSearchBooks.setText(""); loadBooksData(); });
        
        tabbedPane.addTab("Books", booksPanel);
    }
 

    private void createMembersTab() {
        JPanel membersPanel = new JPanel(null);
        
        // Search section
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBorder(new TitledBorder("Search Members"));
        searchPanel.setBounds(20, 20, 1140, 80);
        
        JLabel lblSearchMembers = new JLabel("Search by ID, Name, or Email:");
        lblSearchMembers.setBounds(20, 30, 200, 25);
        lblSearchMembers.setFont(new Font("Dialog", Font.BOLD, 12));
        searchPanel.add(lblSearchMembers);
        
        tfSearchMembers = new JTextField();
        tfSearchMembers.setBounds(230, 30, 200, 25);
        searchPanel.add(tfSearchMembers);
        
        JButton btnSearchMembers = new JButton("Search");
        btnSearchMembers.setBounds(450, 30, 80, 25);
        searchPanel.add(btnSearchMembers);
        
        JButton btnShowAllMembers = new JButton("Show All");
        btnShowAllMembers.setBounds(550, 30, 100, 25);
        searchPanel.add(btnShowAllMembers);
        
        JButton btnClearSearchMembers = new JButton("Clear");
        btnClearSearchMembers.setBounds(670, 30, 80, 25);
        searchPanel.add(btnClearSearchMembers);
        
        membersPanel.add(searchPanel);
        
        // Member form section with explicit field creation
        JPanel memberFormPanel = new JPanel(null);
        memberFormPanel.setBorder(new TitledBorder("Member Information"));
        memberFormPanel.setBounds(20, 120, 600, 200);
        
        // Member ID field
        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setBounds(30, 30, 100, 25);
        lblMemberId.setFont(new Font("Dialog", Font.BOLD, 12));
        memberFormPanel.add(lblMemberId);
        
        tfMemberId = new JTextField();
        tfMemberId.setBounds(140, 30, 150, 25);
        memberFormPanel.add(tfMemberId);
        
        // Name field
        JLabel lblMemberName = new JLabel("Name:");
        lblMemberName.setBounds(30, 70, 100, 25);
        lblMemberName.setFont(new Font("Dialog", Font.BOLD, 12));
        memberFormPanel.add(lblMemberName);
        
        tfMemberName = new JTextField();
        tfMemberName.setBounds(140, 70, 150, 25);
        memberFormPanel.add(tfMemberName);
        
        // Email field
        JLabel lblMemberEmail = new JLabel("Email:");
        lblMemberEmail.setBounds(30, 110, 100, 25);
        lblMemberEmail.setFont(new Font("Dialog", Font.BOLD, 12));
        memberFormPanel.add(lblMemberEmail);
        
        tfMemberEmail = new JTextField();
        tfMemberEmail.setBounds(140, 110, 150, 25);
        memberFormPanel.add(tfMemberEmail);
        
        // Phone field
        JLabel lblMemberPhone = new JLabel("Phone:");
        lblMemberPhone.setBounds(350, 30, 100, 25);
        lblMemberPhone.setFont(new Font("Dialog", Font.BOLD, 12));
        memberFormPanel.add(lblMemberPhone);
        
        tfMemberPhone = new JTextField();
        tfMemberPhone.setBounds(429, 30, 150, 25);
        memberFormPanel.add(tfMemberPhone);
        
        membersPanel.add(memberFormPanel);
        
        // Member actions section
        JPanel memberActionsPanel = new JPanel(null);
        memberActionsPanel.setBorder(new TitledBorder("Actions"));
        memberActionsPanel.setBounds(640, 120, 220, 203);
        
        JButton btnAddMember = new JButton("Add Member");
        btnAddMember.setBounds(20, 40, 180, 30);
        memberActionsPanel.add(btnAddMember);
        
        JButton btnUpdateMember = new JButton("Update Member");
        btnUpdateMember.setBounds(20, 80, 180, 30);
        memberActionsPanel.add(btnUpdateMember);
        
        JButton btnDeleteMember = new JButton("Delete Member");
        btnDeleteMember.setBounds(20, 120, 180, 30);
        memberActionsPanel.add(btnDeleteMember);
        
        JButton btnClearMemberFields = new JButton("Clear Fields");
        btnClearMemberFields.setBounds(20, 160, 180, 25);
        memberActionsPanel.add(btnClearMemberFields);
        
        membersPanel.add(memberActionsPanel);
        
        // Members table
        membersModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        membersModel.setColumnIdentifiers(new String[]{"Member ID", "Name", "Email", "Phone", "Join Date", "Status"});
        
        membersTable = new JTable(membersModel);
        membersTable.setFont(new Font("Dialog", Font.PLAIN, 12));
        membersTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        membersTable.setRowHeight(25);
        
        JScrollPane membersScrollPane = new JScrollPane(membersTable);
        membersScrollPane.setBounds(20, 341, 1140, 369);
        membersPanel.add(membersScrollPane);
        
        // Event listeners
        membersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                populateMemberFields();
            }
        });
        
        tfSearchMembers.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchMembers(tfSearchMembers.getText().trim());
            }
        });
        
        btnAddMember.addActionListener(e -> addMember());
        btnUpdateMember.addActionListener(e -> updateMember());
        btnDeleteMember.addActionListener(e -> deleteMember());
        btnClearMemberFields.addActionListener(e -> clearMemberFields());
        btnSearchMembers.addActionListener(e -> searchMembers(tfSearchMembers.getText().trim()));
        btnShowAllMembers.addActionListener(e -> { tfSearchMembers.setText(""); loadMembersData(); });
        btnClearSearchMembers.addActionListener(e -> { tfSearchMembers.setText(""); loadMembersData(); });
        
        tabbedPane.addTab("Members", membersPanel);
    }

    private void createBorrowingsTab() {
        JPanel borrowingsPanel = new JPanel(null);
        
        // Search section
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBorder(new TitledBorder("Search Borrowings"));
        searchPanel.setBounds(20, 20, 1140, 80);
        
        JLabel lblSearchBorrowings = new JLabel("Search by Member ID or Book ID:");
        lblSearchBorrowings.setBounds(20, 30, 200, 25);
        lblSearchBorrowings.setFont(new Font("Dialog", Font.BOLD, 12));
        searchPanel.add(lblSearchBorrowings);
        
        tfSearchBorrowings = new JTextField();
        tfSearchBorrowings.setBounds(230, 30, 200, 25);
        searchPanel.add(tfSearchBorrowings);
        
        JButton btnSearchBorrowings = new JButton("Search");
        btnSearchBorrowings.setBounds(450, 30, 80, 25);
        searchPanel.add(btnSearchBorrowings);
        
        JButton btnShowAllBorrowings = new JButton("Show All");
        btnShowAllBorrowings.setBounds(550, 30, 100, 25);
        searchPanel.add(btnShowAllBorrowings);
        
        JButton btnShowActive = new JButton("Active Only");
        btnShowActive.setBounds(670, 30, 149, 25);
        searchPanel.add(btnShowActive);
        
        borrowingsPanel.add(searchPanel);
        
        // Borrowing form section
        JPanel borrowingFormPanel = new JPanel(null);
        borrowingFormPanel.setBorder(new TitledBorder("Borrowing Information"));
        borrowingFormPanel.setBounds(20, 120, 600, 241);
        
        // Borrowing ID field - FIXED
        JLabel lblBorrowingId = new JLabel("Borrowing ID:");
        lblBorrowingId.setBounds(30, 40, 100, 25);
        lblBorrowingId.setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingFormPanel.add(lblBorrowingId);
        
        tfBorrowingId = new JTextField();
        tfBorrowingId.setBounds(140, 40, 200, 25);
        tfBorrowingId.setEditable(false);
        borrowingFormPanel.add(tfBorrowingId);
        
        // Member selection
        JLabel lblMemberSelect = new JLabel("Member:");
        lblMemberSelect.setBounds(30, 80, 100, 25);
        lblMemberSelect.setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingFormPanel.add(lblMemberSelect);
        
        cbMemberSelect = new JComboBox<>();
        cbMemberSelect.setBounds(140, 80, 200, 25);
        borrowingFormPanel.add(cbMemberSelect);
        
        // Book selection
        JLabel lblBookSelect = new JLabel("Book:");
        lblBookSelect.setBounds(30, 120, 100, 25);
        lblBookSelect.setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingFormPanel.add(lblBookSelect);
        
        cbBookSelect = new JComboBox<>();
        cbBookSelect.setBounds(140, 120, 200, 25);
        borrowingFormPanel.add(cbBookSelect);
        
        // Borrow Date field - FIXED
        JLabel lblBorrowDate = new JLabel("Borrow Date:");
        lblBorrowDate.setBounds(30, 160, 100, 25);
        lblBorrowDate.setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingFormPanel.add(lblBorrowDate);
        
        tfBorrowDate = new JTextField();
        tfBorrowDate.setBounds(140, 160, 200, 25);
        // Set today's date as default
        tfBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        borrowingFormPanel.add(tfBorrowDate);
        
        // Return Date field - FIXED
        JLabel lblReturnDate = new JLabel("Return Date:");
        lblReturnDate.setBounds(350, 160, 100, 25);
        lblReturnDate.setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingFormPanel.add(lblReturnDate);
        
        tfReturnDate = new JTextField();
        tfReturnDate.setBounds(460, 160, 120, 25);
        borrowingFormPanel.add(tfReturnDate);
        
        borrowingsPanel.add(borrowingFormPanel);
        
        // Borrowing actions section
        JPanel borrowingActionsPanel = new JPanel(null);
        borrowingActionsPanel.setBorder(new TitledBorder("Actions"));
        borrowingActionsPanel.setBounds(640, 120, 220, 241);
        
        JButton btnBorrowBook = new JButton("Borrow Book");
        btnBorrowBook.setBounds(20, 40, 180, 30);
        borrowingActionsPanel.add(btnBorrowBook);
        
        JButton btnReturnBook = new JButton("Return Book");
        btnReturnBook.setBounds(20, 80, 180, 30);
        borrowingActionsPanel.add(btnReturnBook);
        
        JButton btnUpdateBorrowing = new JButton("Update Borrowing");
        btnUpdateBorrowing.setBounds(20, 120, 180, 30);
        borrowingActionsPanel.add(btnUpdateBorrowing);
        
        JButton btnDeleteBorrowing = new JButton("Delete Record");
        btnDeleteBorrowing.setBounds(20, 160, 180, 30);
        borrowingActionsPanel.add(btnDeleteBorrowing);
        
        JButton btnClearBorrowingFields = new JButton("Clear Fields");
        btnClearBorrowingFields.setBounds(20, 201, 180, 25);
        borrowingActionsPanel.add(btnClearBorrowingFields);
        
        borrowingsPanel.add(borrowingActionsPanel);
        
        // Borrowings table
        borrowingsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        borrowingsModel.setColumnIdentifiers(new String[]{"ID", "Member", "Book", "Borrow Date", "Due Date", "Return Date", "Status", "Fine"});
        
        borrowingsTable = new JTable(borrowingsModel);
        borrowingsTable.setFont(new Font("Dialog", Font.PLAIN, 12));
        borrowingsTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        borrowingsTable.setRowHeight(25);
        
        JScrollPane borrowingsScrollPane = new JScrollPane(borrowingsTable);
        borrowingsScrollPane.setBounds(20, 378, 1140, 332);
        borrowingsPanel.add(borrowingsScrollPane);
        
        // Event listeners
        borrowingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                populateBorrowingFields();
            }
        });
        
        tfSearchBorrowings.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchBorrowings(tfSearchBorrowings.getText().trim());
            }
        });
        
        btnBorrowBook.addActionListener(e -> borrowBook());
        btnReturnBook.addActionListener(e -> returnBook());
        btnUpdateBorrowing.addActionListener(e -> updateBorrowing());
        btnDeleteBorrowing.addActionListener(e -> deleteBorrowing());
        btnClearBorrowingFields.addActionListener(e -> clearBorrowingFields());
        btnSearchBorrowings.addActionListener(e -> searchBorrowings(tfSearchBorrowings.getText().trim()));
        btnShowAllBorrowings.addActionListener(e -> { tfSearchBorrowings.setText(""); loadBorrowingsData(); });
        btnShowActive.addActionListener(e -> showActiveBorrowings());
        
        tabbedPane.addTab("Borrowings", borrowingsPanel);
    }
    
    private void createReportsTab() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JButton btnOverdueBooks = new JButton("Overdue Books Report");
        JButton btnMemberActivity = new JButton("Member Activity Report");
        JButton btnBookPopularity = new JButton("Book Popularity Report");
        JButton btnOverviewReport = new JButton("Overview Report");
        JButton btnDetailedReports = new JButton("Detailed Reports Window");
        JButton btnRefreshReports = new JButton("Refresh All Data");
        JButton btnExportReport = new JButton("Export to CSV");
        
        controlPanel.add(btnOverviewReport);
        controlPanel.add(btnOverdueBooks);
        controlPanel.add(btnMemberActivity);
        controlPanel.add(btnBookPopularity);
        controlPanel.add(btnDetailedReports);
        controlPanel.add(btnRefreshReports);
        controlPanel.add(btnExportReport);
        
        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        
        reportsPanel.add(controlPanel, BorderLayout.NORTH);
        reportsPanel.add(reportScrollPane, BorderLayout.CENTER);
        
        // Event listeners for reports
        btnOverviewReport.addActionListener(e -> generateOverviewReportText(reportArea));
        btnOverdueBooks.addActionListener(e -> generateOverdueReportText(reportArea));
        btnMemberActivity.addActionListener(e -> generateMemberActivityReportText(reportArea));
        btnBookPopularity.addActionListener(e -> generateBookPopularityReportText(reportArea));
        btnDetailedReports.addActionListener(e -> generateDetailedReports());
        btnRefreshReports.addActionListener(e -> {
            loadAllData();
            reportArea.setText("All data refreshed successfully.");
        });
        btnExportReport.addActionListener(e -> exportReportToCSV(reportArea.getText()));
        
        // Generate initial overview report
        generateOverviewReportText(reportArea);
        
        tabbedPane.addTab("Reports", reportsPanel);
    }

    // Database connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Load all data
    private void loadAllData() {
        loadBooksData();
        loadMembersData();
        loadBorrowingsData();
        loadComboBoxData();
    }

    // Books CRUD operations
    private void loadBooksData() {
        booksModel.setRowCount(0);
        String sql = "SELECT * FROM books ORDER BY book_id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                booksModel.addRow(new Object[]{
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies")
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

    private void addBook() {
        if (!validateBookInput()) return;
        
        String sql = "INSERT INTO books (book_id, title, author, genre, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int totalCopies = Integer.parseInt(tfBookQuantity.getText().trim());
            
            pstmt.setString(1, tfBookId.getText().trim());
            pstmt.setString(2, tfBookTitle.getText().trim());
            pstmt.setString(3, tfBookAuthor.getText().trim());
            pstmt.setString(4, tfBookGenre.getText().trim());
            pstmt.setInt(5, totalCopies);
            pstmt.setInt(6, totalCopies); // Initially all copies available
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooksData();
            loadComboBoxData();
            clearBookFields();
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                showError("Book ID already exists!");
            } else {
                showError("Error adding book: " + e.getMessage());
            }
        }
    }

    private void updateBook() {
        if (tfBookId.getText().trim().isEmpty()) {
            showWarning("Please select a book to update.");
            return;
        }
        
        if (!validateBookInput()) return;
        
        // Transaction to update book and maintain borrowing integrity
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Check current available copies
                PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT total_copies, available_copies FROM books WHERE book_id = ?"
                );
                checkStmt.setString(1, tfBookId.getText().trim());
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    showWarning("Book not found!");
                    conn.rollback();
                    return;
                }
                
                int currentTotal = rs.getInt("total_copies");
                int currentAvailable = rs.getInt("available_copies");
                int newTotal = Integer.parseInt(tfBookQuantity.getText().trim());
                int borrowedCopies = currentTotal - currentAvailable;
                
                if (newTotal < borrowedCopies) {
                    showWarning("Cannot reduce total copies below borrowed copies (" + borrowedCopies + ")!");
                    conn.rollback();
                    return;
                }
                
                int newAvailable = newTotal - borrowedCopies;
                
                // Update book information
                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE books SET title = ?, author = ?, genre = ?, total_copies = ?, available_copies = ? WHERE book_id = ?"
                );
                updateStmt.setString(1, tfBookTitle.getText().trim());
                updateStmt.setString(2, tfBookAuthor.getText().trim());
                updateStmt.setString(3, tfBookGenre.getText().trim());
                updateStmt.setInt(4, newTotal);
                updateStmt.setInt(5, newAvailable);
                updateStmt.setString(6, tfBookId.getText().trim());
                
                updateStmt.executeUpdate();
                conn.commit();
                
                JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBooksData();
                loadComboBoxData();
                clearBookFields();
                
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            showError("Error updating book: " + e.getMessage());
        }
    }
    
    private void deleteBook() {
        if (tfBookId.getText().trim().isEmpty()) {
            showWarning("Please select a book to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this book?\nThis will also delete all related borrowing records.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        String sql = "DELETE FROM books WHERE book_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tfBookId.getText().trim());
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBooksData();
                loadBorrowingsData();
                loadComboBoxData();
                clearBookFields();
            } else {
                showWarning("Book not found!");
            }
            
        } catch (SQLException e) {
            showError("Error deleting book: " + e.getMessage());
        }
    }
    
    private void searchBooks(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadBooksData();
            return;
        }
        
        booksModel.setRowCount(0);
        String sql = """
            SELECT * FROM books 
            WHERE book_id LIKE ? OR title LIKE ? OR author LIKE ? OR genre LIKE ?
            ORDER BY book_id
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 4; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                booksModel.addRow(new Object[]{
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies")
                });
            }
            
        } catch (SQLException e) {
            showError("Error searching books: " + e.getMessage());
        }
    }
    
    private void populateBookFields() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfBookId.setText(booksModel.getValueAt(selectedRow, 0).toString());
            tfBookTitle.setText(booksModel.getValueAt(selectedRow, 1).toString());
            tfBookAuthor.setText(booksModel.getValueAt(selectedRow, 2).toString());
            tfBookGenre.setText(booksModel.getValueAt(selectedRow, 3).toString());
            tfBookQuantity.setText(booksModel.getValueAt(selectedRow, 4).toString());
        }
    }
    
    private void clearBookFields() {
        tfBookId.setText("");
        tfBookTitle.setText("");
        tfBookAuthor.setText("");
        tfBookGenre.setText("");
        tfBookQuantity.setText("");
    }
    
    private boolean validateBookInput() {
        if (tfBookId.getText().trim().isEmpty() ||
            tfBookTitle.getText().trim().isEmpty() ||
            tfBookAuthor.getText().trim().isEmpty() ||
            tfBookGenre.getText().trim().isEmpty() ||
            tfBookQuantity.getText().trim().isEmpty()) {
            showWarning("Please fill in all fields.");
            return false;
        }
        
        try {
            int quantity = Integer.parseInt(tfBookQuantity.getText().trim());
            if (quantity <= 0) {
                showWarning("Quantity must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showWarning("Quantity must be a valid number.");
            return false;
        }
        
        return true;
    }
    
    // Members CRUD operations
    private void loadMembersData() {
        membersModel.setRowCount(0);
        String sql = "SELECT * FROM members ORDER BY member_id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                membersModel.addRow(new Object[]{
                    rs.getString("member_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDate("join_date"),
                    rs.getBoolean("active_status") ? "Active" : "Inactive"
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading members: " + e.getMessage());
        }
    }
    
    private void addMember() {
        if (!validateMemberInput()) return;
        
        String sql = "INSERT INTO members (member_id, name, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tfMemberId.getText().trim());
            pstmt.setString(2, tfMemberName.getText().trim());
            pstmt.setString(3, tfMemberEmail.getText().trim());
            pstmt.setString(4, tfMemberPhone.getText().trim());
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadMembersData();
            loadComboBoxData();
            clearMemberFields();
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                showError("Member ID or Email already exists!");
            } else {
                showError("Error adding member: " + e.getMessage());
            }
        }
    }
    
    private void updateMember() {
        if (tfMemberId.getText().trim().isEmpty()) {
            showWarning("Please select a member to update.");
            return;
        }
        
        if (!validateMemberInput()) return;
        
        String sql = "UPDATE members SET name = ?, email = ?, phone = ? WHERE member_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tfMemberName.getText().trim());
            pstmt.setString(2, tfMemberEmail.getText().trim());
            pstmt.setString(3, tfMemberPhone.getText().trim());
            pstmt.setString(4, tfMemberId.getText().trim());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMembersData();
                loadComboBoxData();
                clearMemberFields();
            } else {
                showWarning("Member not found!");
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                showError("Email already exists!");
            } else {
                showError("Error updating member: " + e.getMessage());
            }
        }
    }
    
    private void deleteMember() {
        if (tfMemberId.getText().trim().isEmpty()) {
            showWarning("Please select a member to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this member?\nThis will also delete all related borrowing records.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        String sql = "DELETE FROM members WHERE member_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tfMemberId.getText().trim());
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMembersData();
                loadBorrowingsData();
                loadComboBoxData();
                clearMemberFields();
            } else {
                showWarning("Member not found!");
            }
            
        } catch (SQLException e) {
            showError("Error deleting member: " + e.getMessage());
        }
    }
    
    private void searchMembers(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadMembersData();
            return;
        }
        
        membersModel.setRowCount(0);
        String sql = """
            SELECT * FROM members 
            WHERE member_id LIKE ? OR name LIKE ? OR email LIKE ?
            ORDER BY member_id
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 3; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                membersModel.addRow(new Object[]{
                    rs.getString("member_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDate("join_date"),
                    rs.getBoolean("active_status") ? "Active" : "Inactive"
                });
            }
            
        } catch (SQLException e) {
            showError("Error searching members: " + e.getMessage());
        }
    }
    
    private void populateMemberFields() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfMemberId.setText(membersModel.getValueAt(selectedRow, 0).toString());
            tfMemberName.setText(membersModel.getValueAt(selectedRow, 1).toString());
            tfMemberEmail.setText(membersModel.getValueAt(selectedRow, 2).toString());
            tfMemberPhone.setText(membersModel.getValueAt(selectedRow, 3).toString());
        }
    }
    
    private void clearMemberFields() {
        tfMemberId.setText("");
        tfMemberName.setText("");
        tfMemberEmail.setText("");
        tfMemberPhone.setText("");
    }
    
    private boolean validateMemberInput() {
        if (tfMemberId.getText().trim().isEmpty() ||
            tfMemberName.getText().trim().isEmpty() ||
            tfMemberEmail.getText().trim().isEmpty()) {
            showWarning("Please fill in all required fields (ID, Name, Email).");
            return false;
        }
        
        String email = tfMemberEmail.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showWarning("Please enter a valid email address.");
            return false;
        }
        
        return true;
    }
    
    // Borrowings CRUD operations
    private void loadBorrowingsData() {
        borrowingsModel.setRowCount(0);
        String sql = """
            SELECT b.borrowing_id, m.name as member_name, bk.title as book_title,
                   b.borrow_date, b.due_date, b.return_date, b.status, b.fine_amount
            FROM borrowings b
            JOIN members m ON b.member_id = m.member_id
            JOIN books bk ON b.book_id = bk.book_id
            ORDER BY b.borrowing_id DESC
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                borrowingsModel.addRow(new Object[]{
                    rs.getInt("borrowing_id"),
                    rs.getString("member_name"),
                    rs.getString("book_title"),
                    rs.getDate("borrow_date"),
                    rs.getDate("due_date"),
                    rs.getDate("return_date"),
                    rs.getString("status"),
                    String.format("₱%.2f", rs.getDouble("fine_amount"))
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading borrowings: " + e.getMessage());
        }
    }
    
    private void borrowBook() {
        if (cbMemberSelect.getSelectedItem() == null || cbBookSelect.getSelectedItem() == null) {
            showWarning("Please select both member and book.");
            return;
        }
        
        if (tfBorrowDate.getText().trim().isEmpty()) {
            showWarning("Please enter borrow date.");
            return;
        }
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Extract member and book IDs from combo box selections
            String memberSelection = cbMemberSelect.getSelectedItem().toString();
            String bookSelection = cbBookSelect.getSelectedItem().toString();
            
            String memberId = memberSelection.split(" - ")[0];
            String bookId = bookSelection.split(" - ")[0];
            
            // Validate date format
            LocalDate borrowDate;
            try {
                borrowDate = LocalDate.parse(tfBorrowDate.getText().trim());
            } catch (DateTimeParseException e) {
                showWarning("Please enter a valid date in YYYY-MM-DD format.");
                return;
            }
            
            // Check book availability
            try (PreparedStatement checkBook = conn.prepareStatement(
                "SELECT available_copies FROM books WHERE book_id = ?")) {
                checkBook.setString(1, bookId);
                ResultSet rs = checkBook.executeQuery();
                
                if (!rs.next() || rs.getInt("available_copies") <= 0) {
                    showWarning("Book is not available for borrowing!");
                    return;
                }
            }
            
            // Calculate due date (14 days from borrow date)
            LocalDate dueDate = borrowDate.plusDays(14);
            
            // Insert borrowing record
            try (PreparedStatement insertBorrowing = conn.prepareStatement(
                "INSERT INTO borrowings (member_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, 'BORROWED')")) {
                insertBorrowing.setString(1, memberId);
                insertBorrowing.setString(2, bookId);
                insertBorrowing.setDate(3, java.sql.Date.valueOf(borrowDate));
                insertBorrowing.setDate(4, java.sql.Date.valueOf(dueDate));
                insertBorrowing.executeUpdate();
            }
            
            // Update book availability
            try (PreparedStatement updateBook = conn.prepareStatement(
                "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ?")) {
                updateBook.setString(1, bookId);
                updateBook.executeUpdate();
            }
            
            conn.commit();
            
            JOptionPane.showMessageDialog(this, 
                "Book borrowed successfully!\nDue Date: " + dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadBorrowingsData();
            loadBooksData();
            clearBorrowingFields();
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                showError("Error during rollback: " + rollbackEx.getMessage());
            }
            showError("Error borrowing book: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    showError("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    private void returnBook() {
        if (tfBorrowingId.getText().trim().isEmpty()) {
            showWarning("Please select a borrowing record to return.");
            return;
        }
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            int borrowingId = Integer.parseInt(tfBorrowingId.getText().trim());
            
            // Get borrowing details
            try (PreparedStatement getBorrowing = conn.prepareStatement(
                "SELECT book_id, due_date, status FROM borrowings WHERE borrowing_id = ?")) {
                getBorrowing.setInt(1, borrowingId);
                ResultSet rs = getBorrowing.executeQuery();
                
                if (!rs.next()) {
                    showWarning("Borrowing record not found!");
                    return;
                }
                
                if ("RETURNED".equals(rs.getString("status"))) {
                    showWarning("Book is already returned!");
                    return;
                }
                
                String bookId = rs.getString("book_id");
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = LocalDate.now();
                
                // Calculate fine if overdue
                double fine = 0.0;
                if (returnDate.isAfter(dueDate)) {
                    long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
                    fine = overdueDays * 5.0; // ₱5 per day fine
                }
                
                // Update borrowing record
                try (PreparedStatement updateBorrowing = conn.prepareStatement(
                    "UPDATE borrowings SET return_date = ?, status = 'RETURNED', fine_amount = ? WHERE borrowing_id = ?")) {
                    updateBorrowing.setDate(1, java.sql.Date.valueOf(returnDate));
                    updateBorrowing.setDouble(2, fine);
                    updateBorrowing.setInt(3, borrowingId);
                    updateBorrowing.executeUpdate();
                }
                
                // Update book availability
                try (PreparedStatement updateBook = conn.prepareStatement(
                    "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?")) {
                    updateBook.setString(1, bookId);
                    updateBook.executeUpdate();
                }
                
                conn.commit();
                
                String message = "Book returned successfully!";
                if (fine > 0) {
                    message += String.format("\nFine Amount: ₱%.2f", fine);
                }
                
                JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                
                loadBorrowingsData();
                loadBooksData();
                clearBorrowingFields();
            }
            
        } catch (NumberFormatException e) {
            showError("Invalid borrowing ID format.");
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                showError("Error during rollback: " + rollbackEx.getMessage());
            }
            showError("Error returning book: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    showError("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    private void updateBorrowing() {
        // Validate borrowing ID is selected
        if (tfBorrowingId.getText().trim().isEmpty()) {
            showWarning("Please select a borrowing record to update.");
            return;
        }

        // Validate borrow date
        if (tfBorrowDate.getText().trim().isEmpty()) {
            showWarning("Please enter borrow date.");
            return;
        }
        
        // Validate member and book selection
        if (cbMemberSelect.getSelectedItem() == null) {
            showWarning("Please select a member.");
            return;
        }
        
        if (cbBookSelect.getSelectedItem() == null) {
            showWarning("Please select a book.");
            return;
        }

        try {
            // Parse borrowing_id as integer (it's auto-increment)
            int borrowingId;
            try {
                borrowingId = Integer.parseInt(tfBorrowingId.getText().trim());
            } catch (NumberFormatException e) {
                showError("Invalid borrowing ID format. Please enter a valid number.");
                return;
            }
            
            LocalDate borrowDate;
            // Parse and validate borrow date
            try {
                borrowDate = LocalDate.parse(tfBorrowDate.getText().trim());
            } catch (DateTimeParseException e) {
                showWarning("Please enter a valid borrow date in YYYY-MM-DD format.");
                return;
            }

            // Calculate due date (14 days from borrow date)
            LocalDate dueDate = borrowDate.plusDays(14);
            
            // Get member and book IDs as strings (they're VARCHAR in database)
            String memberId;
            String bookId;
            
            try {
                memberId = getMemberIdStringFromComboBox();
            } catch (Exception e) {
                showError("Error getting member ID: " + e.getMessage());
                return;
            }
            
            try {
                bookId = getBookIdStringFromComboBox();
            } catch (Exception e) {
                showError("Error getting book ID: " + e.getMessage());
                return;
            }
            
            // Parse return date if provided and calculate fine
            LocalDate returnDate = null;
            double fine = 0.0;
            String status = "BORROWED"; // Default status
            
            if (!tfReturnDate.getText().trim().isEmpty()) {
                try {
                    returnDate = LocalDate.parse(tfReturnDate.getText().trim());
                    status = "RETURNED"; // If return date is provided, mark as returned
                    
                    // Calculate fine if overdue
                    if (returnDate.isAfter(dueDate)) {
                        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
                        fine = overdueDays * 5.0; // ₱5 per day fine
                    }
                } catch (DateTimeParseException e) {
                    showWarning("Please enter a valid return date in YYYY-MM-DD format.");
                    return;
                }
            } else {
                // If no return date but current date is past due date, calculate ongoing fine
                LocalDate currentDate = LocalDate.now();
                if (currentDate.isAfter(dueDate)) {
                    long overdueDays = ChronoUnit.DAYS.between(dueDate, currentDate);
                    fine = overdueDays * 5.0; // ₱5 per day fine for ongoing overdue
                }
            }

            // Database operations
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);
                
                // Get current book_id and status to check if changes affect availability
                String currentBookId = null;
                String currentStatus = null;
                try (PreparedStatement getCurrentData = conn.prepareStatement(
                        "SELECT book_id, status FROM borrowings WHERE borrowing_id = ?")) {
                    getCurrentData.setInt(1, borrowingId);
                    ResultSet rs = getCurrentData.executeQuery();
                    if (rs.next()) {
                        currentBookId = rs.getString("book_id");
                        currentStatus = rs.getString("status");
                    }
                }

                // Update borrowing record in database
                String sql = "UPDATE borrowings SET member_id = ?, book_id = ?, borrow_date = ?, due_date = ?, return_date = ?, status = ?, fine_amount = ? WHERE borrowing_id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    // Set member_id and book_id as strings (VARCHAR columns)
                    pstmt.setString(1, memberId);
                    pstmt.setString(2, bookId);
                    pstmt.setDate(3, java.sql.Date.valueOf(borrowDate));
                    pstmt.setDate(4, java.sql.Date.valueOf(dueDate));
                    
                    // Set return date or null
                    if (returnDate != null) {
                        pstmt.setDate(5, java.sql.Date.valueOf(returnDate));
                    } else {
                        pstmt.setNull(5, java.sql.Types.DATE);
                    }
                    
                    // Set status and fine amount
                    pstmt.setString(6, status);
                    pstmt.setDouble(7, fine);
                    
                    // Set borrowing_id as integer (it's the primary key)
                    pstmt.setInt(8, borrowingId);
                    
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Handle book availability changes
                        if (currentBookId != null) {
                            // Case 1: Book was changed
                            if (!currentBookId.equals(bookId)) {
                                // Return the old book (increase availability)
                                try (PreparedStatement returnOldBook = conn.prepareStatement(
                                        "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?")) {
                                    returnOldBook.setString(1, currentBookId);
                                    returnOldBook.executeUpdate();
                                }
                                
                                // If new status is BORROWED, decrease availability of new book
                                if ("BORROWED".equals(status)) {
                                    try (PreparedStatement borrowNewBook = conn.prepareStatement(
                                            "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0")) {
                                        borrowNewBook.setString(1, bookId);
                                        int bookUpdateResult = borrowNewBook.executeUpdate();
                                        if (bookUpdateResult == 0) {
                                            conn.rollback();
                                            showWarning("Selected book is not available for borrowing!");
                                            return;
                                        }
                                    }
                                }
                            } 
                            // Case 2: Same book, but status might have changed
                            else if (currentStatus != null && !currentStatus.equals(status)) {
                                if ("BORROWED".equals(currentStatus) && "RETURNED".equals(status)) {
                                    // Book is being returned, increase availability
                                    try (PreparedStatement returnBook = conn.prepareStatement(
                                            "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?")) {
                                        returnBook.setString(1, bookId);
                                        returnBook.executeUpdate();
                                    }
                                } else if ("RETURNED".equals(currentStatus) && "BORROWED".equals(status)) {
                                    // Book is being re-borrowed, decrease availability
                                    try (PreparedStatement borrowBook = conn.prepareStatement(
                                            "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0")) {
                                        borrowBook.setString(1, bookId);
                                        int bookUpdateResult = borrowBook.executeUpdate();
                                        if (bookUpdateResult == 0) {
                                            conn.rollback();
                                            showWarning("Selected book is not available for borrowing!");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        
                        conn.commit();
                        
                        String message = "Borrowing record updated successfully!";
                        if (fine > 0) {
                            message += String.format("\nFine Amount: ₱%.2f", fine);
                        }
                        
                        JOptionPane.showMessageDialog(this, 
                            message, 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Refresh the table and clear form
                        loadBorrowingsData();
                        loadBooksData();
                        clearBorrowingFields();
                    } else {
                        conn.rollback();
                        showWarning("Borrowing record not found! Please check the borrowing ID.");
                    }
                }

            } catch (SQLException e) {
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException rollbackEx) {
                    showError("Error during rollback: " + rollbackEx.getMessage());
                }
                showError("Database error while updating borrowing: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                        showError("Error closing connection: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            showError("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to get member ID as string from combo box
    private String getMemberIdStringFromComboBox() {
        Object selectedItem = cbMemberSelect.getSelectedItem();
        
        if (selectedItem == null) {
            throw new IllegalStateException("No member selected");
        }
        
        String itemString = selectedItem.toString();
        
        // Extract the member ID part from formats like "M005 - Marc Joshua Vigilia"
        String memberIdString;
        if (itemString.contains(" - ")) {
            memberIdString = itemString.split(" - ")[0].trim();
        } else if (itemString.contains(": ")) {
            memberIdString = itemString.split(": ")[0].trim();
        } else {
            memberIdString = itemString.trim();
        }
        
        return memberIdString;
    }

    // Helper method to get book ID as string from combo box
    private String getBookIdStringFromComboBox() {
        Object selectedItem = cbBookSelect.getSelectedItem();
        
        if (selectedItem == null) {
            throw new IllegalStateException("No book selected");
        }
        
        String itemString = selectedItem.toString();
        
        // Extract the book ID part from formats like "B001 - THE GOAT"
        String bookIdString;
        if (itemString.contains(" - ")) {
            bookIdString = itemString.split(" - ")[0].trim();
        } else if (itemString.contains(": ")) {
            bookIdString = itemString.split(": ")[0].trim();
        } else {
            bookIdString = itemString.trim();
        }
        
        return bookIdString;
    }
    
    private void deleteBorrowing() {
        if (tfBorrowingId.getText().trim().isEmpty()) {
            showWarning("Please select a borrowing record to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this borrowing record?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            int borrowingId = Integer.parseInt(tfBorrowingId.getText().trim());
            
            // Get borrowing details to restore book availability if needed
            try (PreparedStatement getBorrowing = conn.prepareStatement(
                "SELECT book_id, status FROM borrowings WHERE borrowing_id = ?")) {
                getBorrowing.setInt(1, borrowingId);
                ResultSet rs = getBorrowing.executeQuery();
                
                if (rs.next()) {
                    String bookId = rs.getString("book_id");
                    String status = rs.getString("status");
                    
                    // Delete borrowing record
                    try (PreparedStatement deleteBorrowing = conn.prepareStatement(
                        "DELETE FROM borrowings WHERE borrowing_id = ?")) {
                        deleteBorrowing.setInt(1, borrowingId);
                        deleteBorrowing.executeUpdate();
                    }
                    
                    // If book was borrowed (not returned), restore availability
                    if ("BORROWED".equals(status)) {
                        try (PreparedStatement updateBook = conn.prepareStatement(
                            "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?")) {
                            updateBook.setString(1, bookId);
                            updateBook.executeUpdate();
                        }
                    }
                }
            }
            
            conn.commit();
            
            JOptionPane.showMessageDialog(this, "Borrowing record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBorrowingsData();
            loadBooksData();
            clearBorrowingFields();
            
        } catch (NumberFormatException e) {
            showError("Invalid borrowing ID format.");
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                showError("Error during rollback: " + rollbackEx.getMessage());
            }
            showError("Error deleting borrowing: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    showError("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    private void searchBorrowings(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadBorrowingsData();
            return;
        }
        
        borrowingsModel.setRowCount(0);
        String sql = """
            SELECT b.borrowing_id, m.name as member_name, bk.title as book_title,
                   b.borrow_date, b.due_date, b.return_date, b.status, b.fine_amount
            FROM borrowings b
            JOIN members m ON b.member_id = m.member_id
            JOIN books bk ON b.book_id = bk.book_id
            WHERE b.member_id LIKE ? OR b.book_id LIKE ? OR m.name LIKE ? OR bk.title LIKE ?
            ORDER BY b.borrowing_id DESC
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 4; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                borrowingsModel.addRow(new Object[]{
                    rs.getInt("borrowing_id"),
                    rs.getString("member_name"),
                    rs.getString("book_title"),
                    rs.getDate("borrow_date"),
                    rs.getDate("due_date"),
                    rs.getDate("return_date"),
                    rs.getString("status"),
                    String.format("₱%.2f", rs.getDouble("fine_amount"))
                });
            }
            
        } catch (SQLException e) {
            showError("Error searching borrowings: " + e.getMessage());
        }
    }
    
    private void showActiveBorrowings() {
        borrowingsModel.setRowCount(0);
        String sql = """
            SELECT b.borrowing_id, m.name as member_name, bk.title as book_title,
                   b.borrow_date, b.due_date, b.return_date, b.status, b.fine_amount
            FROM borrowings b
            JOIN members m ON b.member_id = m.member_id
            JOIN books bk ON b.book_id = bk.book_id
            WHERE b.status = 'BORROWED'
            ORDER BY b.due_date ASC
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                borrowingsModel.addRow(new Object[]{
                    rs.getInt("borrowing_id"),
                    rs.getString("member_name"),
                    rs.getString("book_title"),
                    rs.getDate("borrow_date"),
                    rs.getDate("due_date"),
                    rs.getDate("return_date"),
                    rs.getString("status"),
                    String.format("₱%.2f", rs.getDouble("fine_amount"))
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading active borrowings: " + e.getMessage());
        }
    }
    
    private void populateBorrowingFields() {
        int selectedRow = borrowingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfBorrowingId.setText(borrowingsModel.getValueAt(selectedRow, 0).toString());
            
            // Get the actual member_id and book_id for the selected borrowing
            try {
                int borrowingId = Integer.parseInt(tfBorrowingId.getText());
                
                String sql = "SELECT member_id, book_id, borrow_date, return_date FROM borrowings WHERE borrowing_id = ?";
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setInt(1, borrowingId);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        String memberId = rs.getString("member_id");
                        String bookId = rs.getString("book_id");
                        
                        // Set combo box selections
                        for (int i = 0; i < cbMemberSelect.getItemCount(); i++) {
                            if (cbMemberSelect.getItemAt(i).startsWith(memberId)) {
                                cbMemberSelect.setSelectedIndex(i);
                                break;
                            }
                        }
                        
                        for (int i = 0; i < cbBookSelect.getItemCount(); i++) {
                            if (cbBookSelect.getItemAt(i).startsWith(bookId)) {
                                cbBookSelect.setSelectedIndex(i);
                                break;
                            }
                        }
                        
                        // Set dates
                        if (rs.getDate("borrow_date") != null) {
                            tfBorrowDate.setText(rs.getDate("borrow_date").toString());
                        }
                        if (rs.getDate("return_date") != null) {
                            tfReturnDate.setText(rs.getDate("return_date").toString());
                        } else {
                            tfReturnDate.setText("");
                        }
                    }
                }
                
            } catch (Exception e) {
                showError("Error populating borrowing fields: " + e.getMessage());
            }
        }
    }
    
    private void clearBorrowingFields() {
        tfBorrowingId.setText("");
        cbMemberSelect.setSelectedIndex(-1);
        cbBookSelect.setSelectedIndex(-1);
        tfBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tfReturnDate.setText("");
    }
    
    // Load combo box data
    private void loadComboBoxData() {
        // Load members combo box
        cbMemberSelect.removeAllItems();
        String memberSql = "SELECT member_id, name FROM members WHERE active_status = TRUE ORDER BY name";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(memberSql)) {
            
            while (rs.next()) {
                cbMemberSelect.addItem(rs.getString("member_id") + " - " + rs.getString("name"));
            }
            
        } catch (SQLException e) {
        	// Continuation of the Library Management System code

            showError("Error loading members: " + e.getMessage());
        }
        
        // Load books combo box
        cbBookSelect.removeAllItems();
        String bookSql = "SELECT book_id, title FROM books WHERE available_copies > 0 ORDER BY title";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(bookSql)) {
            
            while (rs.next()) {
                cbBookSelect.addItem(rs.getString("book_id") + " - " + rs.getString("title"));
            }
            
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }
    
 // Text-based report methods for the main tab
    private void generateOverviewReportText(JTextArea reportArea) {
        StringBuilder report = new StringBuilder();
        report.append("=== LIBRARY OVERVIEW REPORT ===\n");
        report.append("Generated on: ").append(new java.util.Date()).append("\n\n");
        
        try (Connection conn = getConnection()) {
            // Total Books
            PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) as total, SUM(total_copies) as copies FROM books");
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            
            // Total Members
            PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) as total FROM members WHERE active_status = TRUE");
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            
            // Active Borrowings
            PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) as total FROM borrowings WHERE status = 'BORROWED'");
            ResultSet rs3 = stmt3.executeQuery();
            rs3.next();
            
            // Overdue Books
            PreparedStatement stmt4 = conn.prepareStatement("SELECT COUNT(*) as total FROM borrowings WHERE status = 'BORROWED' AND due_date < CURDATE()");
            ResultSet rs4 = stmt4.executeQuery();
            rs4.next();
            
            // Total Fines
            PreparedStatement stmt5 = conn.prepareStatement("SELECT COALESCE(SUM(fine_amount), 0) as total FROM borrowings WHERE fine_amount > 0");
            ResultSet rs5 = stmt5.executeQuery();
            rs5.next();
            
            report.append("LIBRARY STATISTICS:\n");
            report.append("- Total Books: ").append(rs1.getInt("total")).append("\n");
            report.append("- Total Copies: ").append(rs1.getInt("copies")).append("\n");
            report.append("- Active Members: ").append(rs2.getInt("total")).append("\n");
            report.append("- Active Borrowings: ").append(rs3.getInt("total")).append("\n");
            report.append("- Overdue Books: ").append(rs4.getInt("total")).append("\n");
            report.append("- Total Outstanding Fines: ₱").append(String.format("%.2f", rs5.getDouble("total"))).append("\n");
            
        } catch (SQLException e) {
            report.append("Error generating report: ").append(e.getMessage());
            showError("Error generating overview report: " + e.getMessage());
        }
        
        reportArea.setText(report.toString());
    }

    private void generateOverdueReportText(JTextArea reportArea) {
        StringBuilder report = new StringBuilder();
        report.append("=== OVERDUE BOOKS REPORT ===\n");
        report.append("Generated on: ").append(new java.util.Date()).append("\n\n");
        
        String sql = """
            SELECT m.name, b.title, br.due_date, 
                   DATEDIFF(CURDATE(), br.due_date) as days_overdue,
                   DATEDIFF(CURDATE(), br.due_date) * 5 as calculated_fine
            FROM borrowings br
            JOIN members m ON br.member_id = m.member_id
            JOIN books b ON br.book_id = b.book_id
            WHERE br.status = 'BORROWED' AND br.due_date < CURDATE()
            ORDER BY br.due_date ASC
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (!rs.next()) {
                report.append("No overdue books found.\n");
            } else {
                report.append(String.format("%-20s %-30s %-12s %-12s %-10s\n", 
                    "Member", "Book Title", "Due Date", "Days Late", "Fine"));
                report.append("-".repeat(90)).append("\n");
                
                do {
                    report.append(String.format("%-20s %-30s %-12s %-12d ₱%.2f\n",
                        truncateString(rs.getString("name"), 19),
                        truncateString(rs.getString("title"), 29),
                        rs.getDate("due_date").toString(),
                        rs.getInt("days_overdue"),
                        rs.getDouble("calculated_fine")));
                } while (rs.next());
            }
            
        } catch (SQLException e) {
            report.append("Error generating report: ").append(e.getMessage());
            showError("Error generating overdue report: " + e.getMessage());
        }
        
        reportArea.setText(report.toString());
    }

    private void generateMemberActivityReportText(JTextArea reportArea) {
        StringBuilder report = new StringBuilder();
        report.append("=== MEMBER ACTIVITY REPORT ===\n");
        report.append("Generated on: ").append(new java.util.Date()).append("\n\n");
        
        String sql = """
            SELECT m.name,
                   COUNT(br.borrowing_id) as total_borrowed,
                   SUM(CASE WHEN br.status = 'BORROWED' THEN 1 ELSE 0 END) as currently_borrowed,
                   COALESCE(SUM(br.fine_amount), 0) as total_fines
            FROM members m
            LEFT JOIN borrowings br ON m.member_id = br.member_id
            WHERE m.active_status = TRUE
            GROUP BY m.member_id, m.name
            ORDER BY total_borrowed DESC
            LIMIT 20
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            report.append(String.format("%-25s %-15s %-15s %-10s\n", 
                "Member Name", "Total Borrowed", "Currently Out", "Total Fines"));
            report.append("-".repeat(75)).append("\n");
            
            while (rs.next()) {
                report.append(String.format("%-25s %-15d %-15d ₱%.2f\n",
                    truncateString(rs.getString("name"), 24),
                    rs.getInt("total_borrowed"),
                    rs.getInt("currently_borrowed"),
                    rs.getDouble("total_fines")));
            }
            
        } catch (SQLException e) {
            report.append("Error generating report: ").append(e.getMessage());
            showError("Error generating member activity report: " + e.getMessage());
        }
        
        reportArea.setText(report.toString());
    }

    private void generateBookPopularityReportText(JTextArea reportArea) {
        StringBuilder report = new StringBuilder();
        report.append("=== BOOK POPULARITY REPORT ===\n");
        report.append("Generated on: ").append(new java.util.Date()).append("\n\n");
        
        String sql = """
            SELECT b.title, b.author, COUNT(br.borrowing_id) as borrow_count
            FROM books b
            LEFT JOIN borrowings br ON b.book_id = br.book_id
            GROUP BY b.book_id, b.title, b.author
            ORDER BY borrow_count DESC
            LIMIT 20
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            report.append(String.format("%-35s %-25s %-15s\n", 
                "Book Title", "Author", "Times Borrowed"));
            report.append("-".repeat(80)).append("\n");
            
            while (rs.next()) {
                report.append(String.format("%-35s %-25s %-15d\n",
                    truncateString(rs.getString("title"), 34),
                    truncateString(rs.getString("author"), 24),
                    rs.getInt("borrow_count")));
            }
            
        } catch (SQLException e) {
            report.append("Error generating report: ").append(e.getMessage());
            showError("Error generating book popularity report: " + e.getMessage());
        }
        
        reportArea.setText(report.toString());
    }

    // Detailed reports window (from your pasted code, fixed)
    private void generateDetailedReports() {
        JFrame reportFrame = new JFrame("Library Reports - Detailed View");
        reportFrame.setSize(900, 700);
        reportFrame.setLocationRelativeTo(this);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane reportTabs = new JTabbedPane();
        
        // Overview Report
        JPanel overviewPanel = createOverviewReportPanel();
        reportTabs.addTab("Overview", overviewPanel);
        
        // Overdue Books Report
        JPanel overduePanel = createOverdueReportPanel();
        reportTabs.addTab("Overdue Books", overduePanel);
        
        // Popular Books Report
        JPanel popularPanel = createPopularBooksReportPanel();
        reportTabs.addTab("Popular Books", popularPanel);
        
        // Member Activity Report
        JPanel activityPanel = createMemberActivityReportPanel();
        reportTabs.addTab("Member Activity", activityPanel);
        
        reportFrame.getContentPane().add(reportTabs);
        reportFrame.setVisible(true);
    }

    private JPanel createOverviewReportPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        try (Connection conn = getConnection()) {
            // Total Books
            PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) as total, SUM(total_copies) as copies FROM books");
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            
            // Total Members
            PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) as total FROM members WHERE active_status = TRUE");
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            
            // Active Borrowings
            PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) as total FROM borrowings WHERE status = 'BORROWED'");
            ResultSet rs3 = stmt3.executeQuery();
            rs3.next();
            
            // Overdue Books
            PreparedStatement stmt4 = conn.prepareStatement("SELECT COUNT(*) as total FROM borrowings WHERE status = 'BORROWED' AND due_date < CURDATE()");
            ResultSet rs4 = stmt4.executeQuery();
            rs4.next();
            
            // Total Fines
            PreparedStatement stmt5 = conn.prepareStatement("SELECT COALESCE(SUM(fine_amount), 0) as total FROM borrowings WHERE fine_amount > 0");
            ResultSet rs5 = stmt5.executeQuery();
            rs5.next();
            
            // Title
            JLabel titleLabel = new JLabel("Library Statistics Overview");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            GridBagConstraints gbc0 = new GridBagConstraints();
            gbc0.gridx = 0; gbc0.gridy = 0; gbc0.anchor = GridBagConstraints.WEST;
            gbc0.insets = new Insets(10, 10, 10, 10);
            panel.add(titleLabel, gbc0);
            
            // Total Books
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0; gbc1.gridy = 1; gbc1.anchor = GridBagConstraints.WEST;
            gbc1.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Total Books: " + rs1.getInt("total")), gbc1);
            
            // Total Copies
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0; gbc2.gridy = 2; gbc2.anchor = GridBagConstraints.WEST;
            gbc2.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Total Copies: " + rs1.getInt("copies")), gbc2);
            
            // Active Members
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0; gbc3.gridy = 3; gbc3.anchor = GridBagConstraints.WEST;
            gbc3.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Active Members: " + rs2.getInt("total")), gbc3);
            
            // Active Borrowings
            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0; gbc4.gridy = 4; gbc4.anchor = GridBagConstraints.WEST;
            gbc4.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Active Borrowings: " + rs3.getInt("total")), gbc4);
            
            // Overdue Books
            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.gridx = 0; gbc5.gridy = 5; gbc5.anchor = GridBagConstraints.WEST;
            gbc5.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Overdue Books: " + rs4.getInt("total")), gbc5);
            
            // Total Fines
            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.gridx = 0; gbc6.gridy = 6; gbc6.anchor = GridBagConstraints.WEST;
            gbc6.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel(String.format("Total Outstanding Fines: ₱%.2f", rs5.getDouble("total"))), gbc6);
            
        } catch (SQLException e) {
            showError("Error generating overview report: " + e.getMessage());
            GridBagConstraints gbcError = new GridBagConstraints();
            gbcError.gridx = 0; gbcError.gridy = 0; gbcError.anchor = GridBagConstraints.WEST;
            gbcError.insets = new Insets(10, 10, 10, 10);
            panel.add(new JLabel("Error loading report data"), gbcError);
        }
        
        return panel;
    }

 // For the overdue report panel:
    private JPanel createOverdueReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Member", "Book", "Due Date", "Days Overdue", "Fine"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        String sql = """
            SELECT m.name, b.title, br.due_date, 
                   DATEDIFF(CURDATE(), br.due_date) as days_overdue,
                   DATEDIFF(CURDATE(), br.due_date) * 5 as calculated_fine
            FROM borrowings br
            JOIN members m ON br.member_id = m.member_id
            JOIN books b ON br.book_id = b.book_id
            WHERE br.status = 'BORROWED' AND br.due_date < CURDATE()
            ORDER BY br.due_date ASC
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("title"),
                    rs.getDate("due_date"),
                    rs.getInt("days_overdue"),
                    String.format("₱%.2f", rs.getDouble("calculated_fine"))
                });
            }
            
        } catch (SQLException e) {
            showError("Error generating overdue report: " + e.getMessage());
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPopularBooksReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Book Title", "Author", "Times Borrowed"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        String sql = """
            SELECT b.title, b.author, COUNT(br.borrowing_id) as borrow_count
            FROM books b
            LEFT JOIN borrowings br ON b.book_id = br.book_id
            GROUP BY b.book_id, b.title, b.author
            ORDER BY borrow_count DESC
            LIMIT 20
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("borrow_count")
                });
            }
            
        } catch (SQLException e) {
            showError("Error generating popular books report: " + e.getMessage());
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMemberActivityReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Member Name", "Total Borrowed", "Currently Borrowed", "Total Fines"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        String sql = """
            SELECT m.name,
                   COUNT(br.borrowing_id) as total_borrowed,
                   SUM(CASE WHEN br.status = 'BORROWED' THEN 1 ELSE 0 END) as currently_borrowed,
                   COALESCE(SUM(br.fine_amount), 0) as total_fines
            FROM members m
            LEFT JOIN borrowings br ON m.member_id = br.member_id
            WHERE m.active_status = TRUE
            GROUP BY m.member_id, m.name
            ORDER BY total_borrowed DESC
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getInt("total_borrowed"),
                    rs.getInt("currently_borrowed"),
                    String.format("₱%.2f", rs.getDouble("total_fines"))
                });
            }
            
        } catch (SQLException e) {
            showError("Error generating member activity report: " + e.getMessage());
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // Utility method for CSV export
    private void exportReportToCSV(String reportText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report to CSV");
        fileChooser.setSelectedFile(new java.io.File("library_report_" + 
            new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileChooser.getSelectedFile())) {
                // Convert the text report to CSV format
                String[] lines = reportText.split("\n");
                for (String line : lines) {
                    if (line.trim().isEmpty() || line.startsWith("===") || line.startsWith("Generated on:")) {
                        continue;
                    }
                    // Simple CSV conversion - replace spaces with commas for data rows
                    if (line.contains("₱") || line.matches(".*\\d+.*")) {
                        writer.println(line.replaceAll("\\s+", ","));
                    } else {
                        writer.println("\"" + line.trim() + "\"");
                    }
                }
                JOptionPane.showMessageDialog(this, "Report exported successfully!");
            } catch (java.io.IOException e) {
                showError("Error exporting report: " + e.getMessage());
            }
        }
    }

    // Utility method to truncate strings for formatting
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
    
    
    // Utility methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
//vigilia, marc joshua c. BSIT 3A