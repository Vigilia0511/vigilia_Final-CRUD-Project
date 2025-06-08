-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 08, 2025 at 03:14 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `book_id` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `author` varchar(100) NOT NULL,
  `genre` varchar(50) NOT NULL,
  `total_copies` int(11) NOT NULL DEFAULT 1,
  `available_copies` int(11) NOT NULL DEFAULT 1,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`book_id`, `title`, `author`, `genre`, `total_copies`, `available_copies`, `created_date`) VALUES
('B001', 'THE GOAT', 'KARTZY', 'action', 5, 5, '2025-06-08 05:45:19'),
('B002', 'TWICE TAGRAM', 'MINA', 'Romance', 3, 3, '2025-06-08 09:41:01'),
('B003', 'Noli Me', 'Dr. Jose Rizal', 'Novel', 10, 9, '2025-06-08 10:15:40'),
('B004', 'advance JAVA', 'Dr. Rajendra Kawale', 'IT', 5, 5, '2025-06-08 12:08:49'),
('B005', 'The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 2, 2, '2025-06-03 04:37:40');

-- --------------------------------------------------------

--
-- Table structure for table `borrowings`
--

CREATE TABLE `borrowings` (
  `borrowing_id` int(11) NOT NULL,
  `member_id` varchar(20) NOT NULL,
  `book_id` varchar(20) NOT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('BORROWED','RETURNED','OVERDUE') DEFAULT 'BORROWED',
  `fine_amount` decimal(10,2) DEFAULT 0.00,
  `created_timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowings`
--

INSERT INTO `borrowings` (`borrowing_id`, `member_id`, `book_id`, `borrow_date`, `due_date`, `return_date`, `status`, `fine_amount`, `created_timestamp`) VALUES
(8, 'M004', 'B005', '2025-06-08', '2025-06-22', '2025-06-08', 'RETURNED', 0.00, '2025-06-08 05:40:55'),
(9, 'M005', 'B003', '2025-06-08', '2025-06-22', '2025-06-30', 'RETURNED', 40.00, '2025-06-08 05:45:45'),
(12, 'M001', 'B002', '2025-06-08', '2025-06-22', '2025-06-08', 'RETURNED', 0.00, '2025-06-08 09:41:53'),
(13, 'M005', 'B003', '2025-06-08', '2025-06-22', NULL, 'BORROWED', 0.00, '2025-06-08 10:16:00'),
(14, 'M005', 'B003', '2025-06-08', '2025-06-22', '2025-06-08', 'RETURNED', 0.00, '2025-06-08 10:17:40'),
(15, 'M004', 'B001', '2025-05-08', '2025-05-22', NULL, 'BORROWED', 85.00, '2025-06-08 11:14:55'),
(16, 'M005', 'B003', '2025-06-08', '2025-06-22', '2025-06-08', 'RETURNED', 0.00, '2025-06-08 12:01:45');

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `member_id` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `join_date` date DEFAULT curdate(),
  `active_status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`member_id`, `name`, `email`, `phone`, `join_date`, `active_status`) VALUES
('M001', 'COLEEN', 'COLEEN@gmail.com', '099707', '2025-06-08', 1),
('M002', 'VIGILIA', 'VIGILIA@gmail.com', '0999999999999', '2025-06-08', 1),
('M003', 'Bob Johnson', 'bob.johnson@email.com', '09632678561', '2025-06-03', 1),
('M004', 'Alice Brown', 'alice.brown@email.com', '09972647381', '2025-06-03', 1),
('M005', 'Marc Joshua Vigilia', 'joshuacajimatvigilia@gmail.com', '09686459583', '2025-06-03', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`);

--
-- Indexes for table `borrowings`
--
ALTER TABLE `borrowings`
  ADD PRIMARY KEY (`borrowing_id`),
  ADD KEY `member_id` (`member_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`member_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `borrowings`
--
ALTER TABLE `borrowings`
  MODIFY `borrowing_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `borrowings`
--
ALTER TABLE `borrowings`
  ADD CONSTRAINT `borrowings_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `borrowings_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
