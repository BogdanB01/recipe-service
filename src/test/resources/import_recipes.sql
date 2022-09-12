delete from recipe;
delete from ingredient;

insert into recipe(id, name, instructions, servings, vegan) values (1, 'Tomato soup', 'This warming vegan soup is made using juicy, ripe tomatoes, which come into season around September....', 4, true);
insert into recipe(id, name, instructions, servings, vegan) values (2, 'Homemade french fries', 'Slice the potatoes 1/2 inch thick. ...', 4, true);
insert into recipe(id, name, instructions, servings, vegan) values (3, 'Grilled steak', 'Place the steaks on the grill and cook until golden brown and slightly charred, 4 to 5 minutes. ...', 2, false);

insert into ingredient(id, name, quantity, recipe_id) values (1, 'tomato', 1.0, 1);
insert into ingredient(id, name, quantity, recipe_id) values (2, 'onion', 0.25, 1);
insert into ingredient(id, name, quantity, recipe_id) values (3, 'garlic', 0.1, 1);
insert into ingredient(id, name, quantity, recipe_id) values (4, 'salt', 0.25, 1);

insert into ingredient(id, name, quantity, recipe_id) values (5, 'potato', 2.0, 2);
insert into ingredient(id, name, quantity, recipe_id) values (6, 'oil', 1.0, 2);
insert into ingredient(id, name, quantity, recipe_id) values (7, 'salt', 0.25, 2);

insert into ingredient(id, name, quantity, recipe_id) values (8, 'steak', 4.0, 3);
insert into ingredient(id, name, quantity, recipe_id) values (9, 'garlic', 0.5, 3);
insert into ingredient(id, name, quantity, recipe_id) values (10, 'pepper', 0.1, 3);
