create table if not exists springtters (
    id bigint primary key auto_increment,
    user_id bigint,
    message varchar(240),
    `date` datetime,
    `image` varchar(120),
    blocked boolean
);

create table if not exists users (
    id bigint primary key auto_increment,
    email varchar(240),
    username varchar(50),
    `password` varchar(50),
    complete_name varchar(120),
    profile_image varchar(120),
    description varchar(512)
);

create table if not exists friendships (
    id bigint primary key auto_increment,
    user_id bigint,
    follower_user_id bigint
);

create table if not exists reactions (
    id bigint primary key auto_increment,
    springtter_id bigint,
    reacted_user_id bigint,
    reaction_id bigint,
    `comment` varchar(240)
);

create table if not exists reaction_types (
    id bigint primary key auto_increment,
    reaction varchar(128)
);