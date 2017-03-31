#Principles of Data Management - Group 8 Phase III

create database PhaseThreeDB;

use PhaseThreeDB;

create table Disease (
name varchar(255) primary key,
cause blob,
mortality_rate decimal(3,1),
transmission varchar(255),
department varchar(255),
prescription int unsigned );

create table Means_Of_Transmission (
means varchar(255) primary key);

create table Outbreak (
id int unsigned auto_increment primary key, 
disease varchar(255),
start date,
end date,
mortality_rate decimal(3,1),
affected int unsigned,
casualties int unsigned,
location int unsigned );

create table Location (
id int unsigned auto_increment primary key,
country varchar(255),
state varchar(255),
city varchar(255) );

create table Prescription (
id int unsigned auto_increment primary key,
description blob,
drug varchar(255) );

create table Drug (
name varchar(255) primary key );

create table CDC_Department (
name varchar(255) primary key );

create table Person (
id int unsigned auto_increment primary key,
name varchar(255),
phone varchar(30),
email varchar(255),
department varchar(255),
research_team int unsigned );

create table Research_Team (
id int unsigned auto_increment primary key,
name varchar(255) );

create table Publication (
disease varchar(255),
research_team int unsigned,
year_published date,
constraint pk_publication 
primary key (year_published, disease, research_team) );

###
#Add constraints to link tables together
###

alter table Outbreak
add constraint fk_disease_outbreak
foreign key (disease) references Disease(name);

alter table Outbreak
add constraint fk_location
foreign key (location) references Location(id);

alter table Disease
add constraint fk_transmission
foreign key (transmission) references Means_Of_Transmission(means);

alter table Disease
add constraint fk_department_disease
foreign key (department) references CDC_Department(name);

alter table Disease
add constraint fk_prescription
foreign key (prescription) references Prescription(id);

alter table Prescription
add constraint fk_drug
foreign key (drug) references Drug(name);

alter table Person
add constraint fk_department_person
foreign key (department) references CDC_Department(name);

alter table Person
add constraint fk_researchteam_person
foreign key (research_team) references Research_Team(id);

alter table Publication
add constraint fk_disease_publication
foreign key (disease) references Disease(name);

alter table Publication
add constraint fk_researchteam_publication
foreign key (research_team) references Research_Team(id);

###
#Building records
###

#means

insert into means_of_transmission (means) values 
('air'),
('food'), 
('water'), 
('birds'), 
('mosquitoes'), 
('ticks'), 
('none');

#department

insert into cdc_department(name) values 
('Office of Infectious Diseases'),
('Office of Minority Health'),
('Office of Noncommunicable Diseases'),
('Office of Public Health Preparedness'), 
('Office of Public Health Science Services'),
('Office of Mental Health and Disorders'),
('Office of Made-Up Diseases'), 
('Office of Victims of Random Names'), 
('The Department'),
('Another Department'), 
('One More Department'), 
('Last Department');


#research team

insert into research_team (name) values 
('Lyme Disease Research Group'), 
('RNA Function in Pathogens'),
('Redirecting Cell Death'),
('Hippocampus Disorders'),
('Analyzing E. Coli Lifecycles');

#person

insert into Person (name, phone, email, department, research_team) values
('Zachary Migliorini', '111-555-1111', 'zmm2962@rit.edu', 'Office of Infectious Diseases', 1),
('Jeffrey Bauer', '555-555-1195', 'jcb6017@rit.edu', 'Office of Minority Health', 2),
('Nicholas Stokowski', '555-555-5555 EXT 1119', 'nks4601@rit.edu', 'Office of Noncommunicable Diseases', 3),
('Carlos Rivero', '555-555-5555 12', 'crr@cs.rit.edu', 'Office of Public Health Preparedness', 4),
('Jonathan Joeseph', '+04-111-555-1135', null, null, null),
('Joesephine Joeseph', '+04-111-555-1136', null, 'Office of Minority Health', null),
('Jonathan Junior', '+04-111-555-1134', null, null, 4),
('Bob Ross', null, 'bob@ross.com', 'Office of Mental Health and Disorders', 4);


#location 

insert into location (country, state, city) values 
('United States', 'New York', 'Rochester'),
('United States', 'California', 'San Francisco'),
('Kenya', null , 'Nairobi'),
('Germany', 'Bavaria', 'Munich'),
('Germany', 'Hesse', 'Weisbaden'),
('Germany', 'Bremen', 'Bremen'),
('Germany', 'Bavaria', 'Regensburg'),
('United States', 'Ohio', 'Colombus'),
('United States', 'New York', 'Buffalo'), 
('United States', 'New York', 'Albany'),
('United States', 'New York', 'Syracuse'),
('United States', 'New York', 'Ronkonkoma');


#drug

insert into drug (name) values 
('Penecillin'),
('Seroquel'),
('Advil'),
('Tylenol'),
('Hydrocodone'),
('Chemical X'),
('Morphine'),
('Aspirin'),
('Mezlin'),
('Ticar'),
('Nallpen'),
('Cloxapen'),
('Geopen'),
('Pipracil'),
('Dynapen'),
('Spectrobid'),
('Amoxil');

#prescription

insert into prescription (description, drug) values
('Twice a day for two weeks', 'Penecillin'),
('Nightly before bed', 'Tylenol'),
('Whenever pain arises, no more than twice a day', 'Advil'),
('Mondays, Thursdays, and Sundays, twice a day with a meal.', 'Hydrocodone'),
('Once every other week. No food and only 8 oz or less of water for 24 hours before.', 'Amoxil'),
('Two sets of three pills a day with a meal.', 'Dynapen'),
('Once a day in the morning before eating.' , 'Pipracil'),
('Twice a day for two weeks', 'Geopen'),
('Twice a day fro four weeks', 'Mezlin');




#disease

insert into disease (name, cause, mortality_rate, transmission, department, prescription) values
('E. Coli', 'Escherichia coli - a bacteria found in the digestive tracts of both humans and animals.',
	5, 'food', 'Office of Infectious Diseases', 1),
('Headache', 'One or many factors, such as: dehydration, stress, lack of sleep, malnutrition.', 0.00, 
	'none', 'Office of Noncommunicable Diseases', 3),
('Psychosis', 'Deterioration of neurons in hippocampus. Exacerbated by many social conditions and drug use.', 0.00,
	'none', 'Office of Mental Health and Disorders', 2),
('Lyme Disease', 'Borrelia burgdorferi - a bacteria found solely in deer ticks, often during the summer months.', 
	1, 'ticks', 'Office of Public Health Preparedness', null),
('Ebola', 'Caused by different strains of ebolaviruses, a virulogical taxon composed of five species of virus.',
	50, 'water', 'Office of Infectious Diseases', null),
('Whooping Cough', 'Bordetella pertussis - an extremely contagious bacteria only found in humans.', 1,
	'air', 'Office of Infectious Diseases', 4),
('Avian Flu', 'Caused by strains of the Influenza A virus.', 60, 'birds', 
	'Office of Infectious Diseases', 8),
('Salmonella', 'Caused by Salmonella bacteria, often from undercooked poultry.', 1, 'food', 
	'Office of Public Health Preparedness', 6),
('Tuberculosis', 'Mycobacterium tuberculosis - A bacteria whichi usually attacks the lungs.', 1, 'air',
	'Office of Public Health Science Services', 5),
('Malaria', 'Caused by parasitic protozoans of the genus Plasmodium.', 25, 'mosquitoes', 
	'Office of Public Health Preparedness', 9);
	

#outbreak

insert into outbreak (disease, start, end, mortality_rate, affected, casualties, location) values
('Ebola', '2015-02-01', null, 39.0, 28500, 11300, 3),
('Lyme Disease', '2011-06-14', '2011-09-01', 3.0, 1200, 36, 1),
('Ebola', '1944-01-05', '1949-12-30', 53.0, 18000, 9540, 1),
('Avian Flu', '1974-03-01', '1975-11-20', 86.0, 11000, 7980, 1),
('Avian Flu', '1971-11-05', '1972-07-22', 16.0, 11000, 7980, 1),
('Malaria', '2001-04-24', '2001-08-13', 40, 20000, 8000, 3),
('Tuberculosis', '2005-10-13', '2006-02-04', 10, 18440, 1850, 4),
('Lyme Disease', '2004-11-24', '2005-02-13', 3, 158, 5, 6),
('Malaria', '1985-06-14', '1987-03-19', 80, 13500, 10800, 5),
('Salmonella', '2008-09-10', '2009-02-17', 0, 1600, 0, 7);



#publication

insert into publication (disease, research_team, year_published) values 
('Lyme Disease', 1, '2011-01-01'), 
('E. Coli', 5, '2009-01-01'),
('Psychosis', 4, '1994-01-01'),
('Ebola', 4, '1999-01-01');  


















