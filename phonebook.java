import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class phonebook {

    static Entry[] contactList;
    static int num_entries;
    static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) throws Exception {

        int i; char C;
        String code, Command;
        contactList = new Entry[200];
        num_entries = 0;

        try {
            readPhoneBook("PhoneBook1.txt");

        } catch (FileNotFoundException e) {
            //System.out.println("Error has occurred - File not found.");
        }

        System.out.println("Codes are entered as 1 to 8 characters.\n" +
                
                "\nUse Commands:\n" +

                "*----------------------------------------------*\n" +
                " \"e\" for entering a new contact,\n" +
                "*----------------------------------------------*\n" +
                " \"f\" to find a contact by first name,\n" +
                " \"r\" to find a contact by last name,\n" +
                " \"y\" to find a contact by phone number,\n" +
                "*----------------------------------------------*\n" +
                " \"l\" for listing all the entries,\n" +
                " \"m\" for merging entries,\n" +
                " \"d\" for removing contacts by phone number,\n" +
                "*----------------------------------------------*\n" +
                " \"a\" for sorting by first name,\n" +
                " \"n\" for sorting by last name,\n" +
                " \"p\" for sorting by phone number,\n" +
                "*----------------------------------------------*\n" +
                " \"q\" to quit.\n");

        C = ' ';

        while (C != 'q') {
            System.out.print("Command: ");
            Command = sc.next();
            C = Command.charAt(0);

            switch (C) {
                case 'e':
                    addContact();

                    break;

                case 'f':
                    System.out.print("Enter first name: ");
                    code = sc.next();
                    sc.nextLine();
                    i = index(code);

                    if (i >= 0) {
                        displayContact(contactList[i]);
                    } else {
                        System.out.println("**No entry with code " + code);
                    }
                    break;

                case 'r':
                    System.out.print("Enter last name: ");
                    code = sc.next();
                    sc.nextLine();
                    i = index1(code);

                    if (i >= 0) {

                        displayContact(contactList[i]);
                    } else {

                        System.out.println("**No entry with code " + code);
                    }
                    break;

                case 'y':
                    System.out.print("Enter phone number: ");
                    code = sc.next();
                    sc.nextLine();
                    i = index2(code);

                    if (i >= 0) {
                        displayContact(contactList[i]);
                    } else {
                        System.out.println("**No entry with code " + code);
                    }
                    break;

                case 'l':
                    listAllContacts();
                    break;

                case 'q':
                    CopyPhoneBookToFile("PhoneBook1.txt");
                    System.out.println("Quitting the application. All the entries are " +
                            "stored in the file PhoneBook1.txt");
                    break;

                case 'a':
                    sortListByFirstName();
                    break;

                case 'n':
                    sortListByLastName();
                    break;

                case 'p':
                    sortListByPhoneNumber();
                    break;

                case 'd':
                    break;

                case 'm':
                    mergeContacts();
                    break;

                default:
                    System.out.println("Invalid command Please enter the command again!!!");
            }
        }
    }

    private static void mergeContacts() {
        System.out.println("Merging contacts..");

        /*

         * Creating a Map to store firstname, and lastname as key, and number and notes

         * as value for the map

         */

        Map<String, String> contactMap = new HashMap<>();

        for (int i = 0; i < num_entries; i++) {

            /*

             * If same entry is found, we append the previous notes and add it to

             * the new notes, and override the existing entry..

             */

            if (contactMap.containsKey(contactList[i].firstname + " " + contactList[i].lastname)) {
                String value = contactMap.get(contactList[i].firstname + " " + contactList[i].lastname);
                String[] split = value.split(" ");
                String previousNumber = split[0];
                String previousnotes = "";

                if (split.length > 2) {
                    int j = 1;
                    for (; j < split.length - 1; j++) {
                        previousnotes += split[j] + " ";
                    }
                    previousnotes += split[j];
                } else {
                    previousnotes = split[1];
                }

                String newNumber = contactList[i].number;

                if (previousNumber.equals(newNumber)) {
                    String newnotes = contactList[i].notes;
                    newnotes = newnotes + "\n" + previousnotes;
                    contactMap.put(contactList[i].firstname + " " + contactList[i].lastname,
                            contactList[i].number + " " + newnotes);
                } else {
                    String newnotes = contactList[i].notes;

                    newnotes = newnotes + "\n" + previousnotes + "\n" + previousNumber;
                    contactMap.put(contactList[i].firstname + " " + contactList[i].lastname,
                            contactList[i].number + " " + newnotes);
                }

            } else {

                contactMap.put(contactList[i].firstname + " " + contactList[i].lastname,
                        contactList[i].number + " " + contactList[i].notes);

            }

        }

        num_entries = contactMap.size();

        int i = 0;

        for (Map.Entry<String, String> entry : contactMap.entrySet()) {
            String key = entry.getKey();
            String[] split = key.split(" ");
            String value = entry.getValue();
            String[] split2 = value.split(" ");

            contactList[i].firstname = split[0];
            contactList[i].lastname = split[1];
            contactList[i].number = split2[0];

            String notes = "";

            if (split2.length > 2) {
                int j = 1;
                for (; j < split2.length - 1; j++) {
                    notes += split2[j] + " ";
                }
                notes += split2[j];
            } else {
                notes = split2[i];
            }

            contactList[i].notes = notes;
            i++;
        }
        System.out.println("Contacts merged!");
    }

    private static int index2(String Key) {

// Function to get the index of a key from an array
// if not found, returns -1
        for (int i = 0; i < num_entries; i++) {
            if (contactList[i].number.equalsIgnoreCase(Key))
                return i; // Found the Key, return index.
        }
        return -1;
    }

    private static int index1(String Key) {

// Function to get the index of a key from an array
// if not found, returns -1
        for (int i = 0; i < num_entries; i++) {
            if (contactList[i].lastname.equalsIgnoreCase(Key))
                return i; // Found the Key, return index.
        }
        return -1;
    }

    private static void sortListByPhoneNumber() {

        int i;
        Entry temp;

        for (int j = 0; j < num_entries; j++) {
            for (i = j + 1; i < num_entries; i++) {
                if (contactList[j].number.compareToIgnoreCase(contactList[i].number) > 0) {

                    temp = contactList[j];
                    contactList[j] = contactList[i];
                    contactList[i] = temp;

                }
            }
        }
        listAllContacts();
    }

    public static void readPhoneBook(String FileName) throws Exception {

        File F;
        F = new File(FileName);
        Scanner S = new Scanner(F);

        while (S.hasNextLine()) {
            contactList[num_entries] = new Entry();
            contactList[num_entries].firstname = S.next();
            contactList[num_entries].lastname = S.next();
            contactList[num_entries].number = S.next();
            contactList[num_entries].notes = S.nextLine();

            num_entries++;
        }
        System.out.println("Entry Saved");
        S.close();
    }

    /**
     *Makes user add first and last name with correct length
     */
    public static void addContact() {

        System.out.print("Enter first name: ");
        String firstname = sc.next();

        while (true) {

            if (firstname.length() > 8) {
                System.out.println("First name can only hold 8 characters");
            }

            if (firstname.length() <= 8) {
                break;
            }

            System.out.print("Enter first name: ");
            firstname = sc.next();
        }

        System.out.print("Enter last name: ");
        String lastname = sc.next();

        while (true) {

            if (lastname.length() > 8) {
                System.out.println("Last name can only hold 8 characters");
            }

            if (lastname.length() <= 8) {
                break;
            }

            System.out.print("Enter last name: ");
            lastname = sc.next();
        }

        String number;
        sc.nextLine();

        String pattern = "^\\(?(\\d{3})?\\)?[- ]?(\\d{3})[- ](\\d{4})$";

        contactList[num_entries] = new Entry();
        contactList[num_entries].firstname = firstname;
        contactList[num_entries].lastname = lastname;
        System.out.print("Enter Number: ");
        number = sc.nextLine();

        //makes sure number is of correct pattern/format
        while (true) {

            if (number.matches(pattern)) {
                contactList[num_entries].number = number;
                break;

            } else {
                System.out.println("Error!");
                System.out.println("Incorrect format. Make sure your number matches the \"012-3456\"," +
                        "\"(012)345-6789\"" +
                        ", or \"012-345-6789\" format.");
                System.out.print("Enter number: ");
                number = sc.nextLine();
            }
        }

        System.out.print("Enter notes: ");
        contactList[num_entries].notes = sc.nextLine();
        num_entries++;
        System.out.println(" ");
    }

    // Function to get the index of a key from an array
    public static int index(String Key) {
        for (int i = 0; i < num_entries; i++) {
            if (contactList[i].firstname.equalsIgnoreCase(Key))
                return i; // Found the Key, return index.
        }
        //when not found
        return -1;
    }

    public static void displayContact(Entry contact) {
        System.out.println("--" + contact.firstname + "\t");
        System.out.println("--" + contact.lastname + "\t");
        System.out.println("--" + contact.number + "\t");
        System.out.println("--" + contact.notes);
        System.out.println(" ");
    }

    public static void listAllContacts() {
        int i = 0;
        while (i < num_entries) {
            displayContact(contactList[i]);
            i++;
        }
    }

    public static void sortListByFirstName() {
        int i;
        Entry temp;

        for (int j = 0; j < num_entries; j++) {
            for (i = j + 1; i < num_entries; i++) {
                if (contactList[j].firstname.compareToIgnoreCase(contactList[i].firstname) > 0) {
                    temp = contactList[j];
                    contactList[j] = contactList[i];
                    contactList[i] = temp;
                }
            }
        }
        listAllContacts();
    }

    private static void sortListByLastName() {
        int i;
        Entry temp;

        for (int j = 0; j < num_entries; j++) {
            for (i = j + 1; i < num_entries; i++) {
                if (contactList[j].lastname.compareToIgnoreCase(contactList[i].lastname) > 0) {
                    temp = contactList[j];
                    contactList[j] = contactList[i];
                    contactList[i] = temp;
                }
            }
        }
        listAllContacts();
    }

    public static void CopyPhoneBookToFile(String FileName) throws Exception {
        FileOutputStream out = new FileOutputStream(FileName);
        PrintStream P = new PrintStream(out);

        for (int i = 0; i < num_entries; i++) {
            P.println(contactList[i].firstname + "\t" + contactList[i].lastname + "\t" +
                    contactList[i].number + "\t" + contactList[i].notes);
        }
    }
}
