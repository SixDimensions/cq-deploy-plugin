This release will deploy the code to the sonatype source code repository 
for release to the maven central repository.

The key things to know are:
	1. The code will be signed with a PGP key located at /home/admin01/.gnupg,
		the passphrase for this key is 'bishopranch2'
	2. The code will be released under the user in the ~/.m2/settings.xml,
		at the moment this is klcodanr, this might need to be changed.