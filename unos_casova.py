import json

podaci = {}
lista_podataka = []
ime_fajla = "casovi.json"

try:
	with open(ime_fajla,"r") as f:
			data = json.load(f)
except IOError:
	print("Greska pri ucitavanju datoteke!\n")


while(True):
	ime = input("Unesite ime predmeta ili quit za izlazak: \n")
	if ime == 'quit':
		break
	podaci["ime"] = str(ime)
	podaci["predavac"] = input("Unesite ime predavaca: \n")
	podaci["mesto"] = input("Unesite mesto odrazavanja: \n")
	podaci["dan"] = input("Unesite ime dana u nedelji: \n")
	podaci["pocetak"] = input("Unesite pocetak odrzavanja kursa: \n")
	podaci["kraj"] = input("Unesite kraj odrzavanja kursa: \n")
	podaci["grupa"] = input("Unesite ime grupe: \n")	
	podaci["godina"] = podaci["grupa"][0]
	podaci["tok"] = podaci["grupa"][:2]
	data.append(podaci)
	podaci = {}

try:
	with open(ime_fajla, "w") as f:
		json.dump(data,f)
except IOError:
	print("Greska pri ucitavanju datoteke!\n")
