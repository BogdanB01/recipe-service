drop table RECIPE if exists;

create table RECIPE(
    ID bigint not null AUTO_INCREMENT,
    NAME varchar(128),
    INSTRUCTIONS varchar(512),
    SERVINGS int,
    VEGAN boolean,
    PRIMARY KEY (ID)
);

drop table INGREDIENT if exists;

create table INGREDIENT(
    ID bigint not null AUTO_INCREMENT,
    NAME varchar(128),
    QUANTITY double,
    RECIPE_ID bigint references RECIPE(ID) on delete cascade,
    PRIMARY KEY (ID)
);

drop sequence hibernate_sequence if exists;
create sequence hibernate_sequence;