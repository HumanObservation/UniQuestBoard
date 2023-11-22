-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： localhost
-- 產生時間： 2023 年 11 月 21 日 21:58
-- 伺服器版本： 10.4.28-MariaDB
-- PHP 版本： 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `uniquestboard`
--

-- --------------------------------------------------------

--
-- 資料表結構 `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `description` varchar(200) NOT NULL,
  `publisher` varchar(20) NOT NULL,
  `address` varchar(50) NOT NULL,
  `image_url` varchar(100) DEFAULT NULL,
  `publish_date` datetime NOT NULL,
  `remain_time` int(11) NOT NULL,
  `contact` varchar(40) NOT NULL,
  `reward` int(11) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `title`, `description`, `publisher`, `address`, `image_url`, `publish_date`, `remain_time`, `contact`, `reward`, `status`) VALUES
(1, 1, 'I need coffee QQ', 'Please buy a Starbucks green tea latte for me, thx', 'ivanaw', 'LG1 1353', NULL, '2023-11-30 13:23:44', 1, '+852 9375 1234', 50, 'PENDING'),
(2, 3, 'My hunger grows..', 'Please buy a Mc Big Mac set with mid cola, thx a lot', 'trxc', 'LG4 406', NULL, '2023-11-30 14:33:13', 1, 'IG:@qwq_886', 100, 'PENDING');

-- --------------------------------------------------------

--
-- 資料表結構 `quest`
--

CREATE TABLE `quest` (
  `quest_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `quest`
--

INSERT INTO `quest` (`quest_id`, `order_id`, `user_id`) VALUES
(1, 1, 2),
(2, 2, 4);

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `itsc` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `profilepic_url` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `user`
--

INSERT INTO `user` (`user_id`, `student_id`, `itsc`, `email`, `password`, `profilepic_url`) VALUES
(1, 20791234, 'ivanaw', 'ivanaw@connect.ust.hk', '123456', 'www'),
(2, 20791455, 'rtxc', 'rtxciu@connect.ust.hk', '888', NULL),
(3, 20791255, 'trxc', 'trxcui@connect.ust.hk', '777', NULL),
(4, 20792312, 'kami', 'kamida@connect.ust.hk', '123', NULL);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- 資料表索引 `quest`
--
ALTER TABLE `quest`
  ADD PRIMARY KEY (`quest_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `order_id` (`order_id`);

--
-- 資料表索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `quest`
--
ALTER TABLE `quest`
  MODIFY `quest_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- 資料表的限制式 `quest`
--
ALTER TABLE `quest`
  ADD CONSTRAINT `quest_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `quest_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
