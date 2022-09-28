# Foobar

Read me for Car Toll Api
this project will help get inquiry car violation
## Installation


## Key Generator section
for generate keys must do this steps :

1 . install openssl - for this keys must install version 1.1.1 . this version will generate pkcs#1
2 . generate private key

```bash
openssl genrsa -f4 -out private.pem 2048
```

3. generate public key from this private

```bash
openssl rsa -in private.txt -outform PEM -pubout -out public.txt
```

4. generate certificate with private key - must be x509

```bash
openssl req -nodes -newkey rsa:2048 -keyout private.pem -out cert.crt -x509 -days 36500
```

5. generate keystore and store in some url that will write in yml file

```bash
openssl pkcs12 -export -out keystore.p12 -in cert.crt -inkey private.pem -name naji
```
(naji in here is alias - must be same with certificate)

for more info :
https://www.freecodecamp.org/news/openssl-command-cheatsheet-b441be1e8c4a/
https://www.digitalocean.com/community/tutorials/openssl-essentials-working-with-ssl-certificates-private-keys-and-csrs
https://stackoverflow.com/questions/69750026/create-sha256withrsa-in-two-steps
https://www.scottbrady91.com/openssl/creating-rsa-keys-using-openssl
https://stackoverflow.com/questions/10783366/how-to-generate-pkcs1-rsa-keys-in-pem-format
https://stackoverflow.com/questions/70266909/create-pkcs1-formatted-rsa-key-using-openssl-v3-0-0
https://rietta.com/blog/openssl-generating-rsa-key-from-command/




## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
HamBam Team