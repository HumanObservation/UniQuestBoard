-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2023-12-02 08:25:13
-- 服务器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `uniquestboard`
--

-- --------------------------------------------------------

--
-- 表的结构 `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `description` varchar(200) NOT NULL,
  `publisher` varchar(20) NOT NULL,
  `publish_date` varchar(30) NOT NULL,
  `expired_date` varchar(30) NOT NULL,
  `contact` varchar(40) NOT NULL,
  `reward` varchar(30) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `title`, `description`, `publisher`, `publish_date`, `expired_date`, `contact`, `reward`, `status`) VALUES
(1, 1, 'I need coffee QQ', 'Please buy a Starbucks green tea latte for me, thx', 'ivanaw', '2023/11/30 13:23', '2023/11/30 14:23', '9375 1234', '50', '1'),
(2, 3, 'My hunger grows..', 'Please buy a Mc Big Mac set with mid cola, thx a lot', 'trxc', '2023/11/30 14:33', '2023/11/30 14:43', 'IG:@qwq_886', '100', '2'),
(3, 4, 'Urgent!! Pineapplepen', 'Urgent!! Please bring me a pen. I will have a exam after 10 mins....', 'kami', '2023/11/30 01:41', '2023/12/01 01:50', '9375 1234', 'free lunch', '5'),
(4, 4, 'Need IT help', 'My laptop is broken and idk what happens..', 'kami', '2023/12/01 01:45', '2023/12/01 01:48', '3333 5555', '$100', '5'),
(5, 9, 'SOS Tissue', 'I am in LG1 Man WC SOS', 'pvpaa', '2023/12/01 01:50', '2023/12/02 01:50', '9853 2323', '$500', '2'),
(6, 9, 'Order Genki Together for Dinne', 'As title said', 'pvpaa', '2023/12/02 06:53', '2023/12/03 19:00', '44447777', 'lower delivery fee', '0'),
(7, 10, 'SOS! Need a Calculator', 'exam start in half an hour.', 'tatee', '2023/12/02 07:00', '2023/12/03 15:25', '66663333', 'a free meal/starbucks', '0'),
(8, 5, 'PD100w typc-c charger', 'can anyone borrow me a charger? my laptop will die in 3 min', 'ovoaw', '2023/12/02 07:07', '2023/12/03 07:07', '44448888', 'free drink', '0'),
(9, 2, 'Starving......Hall4', 'thanks to typhoon I have been stayed in hall for 2 days. does anyone have extra food in hall4 ?', 'rtxc', '2023/12/02 07:11', '2023/12/03 15:08', '22226547', '3 times of the original price', '0'),
(10, 2, 'Play \"it takes two\"', 'Anyone wants to play this game together? I will provide the pc and gamepad', 'rtxc', '2023/12/02 07:15', '2023/12/03 15:12', '88884444', 'have fun', '2');

-- --------------------------------------------------------

--
-- 表的结构 `quest`
--

CREATE TABLE `quest` (
  `quest_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `quest`
--

INSERT INTO `quest` (`quest_id`, `order_id`, `user_id`) VALUES
(1, 1, 2),
(2, 2, 4),
(4, 5, 4),
(5, 10, 11);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `itsc` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`user_id`, `student_id`, `itsc`, `email`, `password`) VALUES
(1, 20791234, 'ivanaw', 'ivanaw@connect.ust.hk', '123456'),
(2, 20791455, 'rtxc', 'rtxciu@connect.ust.hk', '888'),
(3, 20791255, 'trxc', 'trxcui@connect.ust.hk', '777'),
(4, 20792312, 'kami', 'kamida@connect.ust.hk', '123'),
(5, 20792111, 'ovoaw', 'ovoaw@connect.ust.hk', '123'),
(6, 20792113, 'pvpwa', 'pvpwa@connect.ust.hk', '123'),
(7, 20792211, 'wowae', 'wowae@connect.ust.hk', '123'),
(8, 20792341, 'vovwa', 'vovwa@connect.ust.hk', '123'),
(9, 20793111, 'pvpaa', 'pvpaa@connect.ust.hk', '123'),
(10, 20794312, 'tatee', 'tatee@connect.ust.hk', '123'),
(11, 20761234, 'bhuang', 'bhuang@connect.ust.hk', '654321');

--
-- 转储表的索引
--

--
-- 表的索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- 表的索引 `quest`
--
ALTER TABLE `quest`
  ADD PRIMARY KEY (`quest_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `order_id` (`order_id`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `quest`
--
ALTER TABLE `quest`
  MODIFY `quest_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- 限制导出的表
--

--
-- 限制表 `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- 限制表 `quest`
--
ALTER TABLE `quest`
  ADD CONSTRAINT `quest_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `quest_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
