MySQL 5.1 supports two character sets for storing Unicode data:

 ```
 ucs2, the UCS-2 encoding of the Unicode character set using 16 bits per character
 utf8, a UTF-8 encoding of the Unicode character set using one to three bytes per character
 ```

In version 5.5 some new character sets where added:

 ```
 utf8mb4, a UTF-8 encoding of the Unicode character set using one to four bytes per character
 ucs2 and utf8 support BMP characters. utf8mb4, utf16, and utf32 support BMP and supplementary characters.
 ```

 ```
 To use 4-byte UTF8 with Connector/J configure the MySQL server with character_set_server=utf8mb4.
 Connector/J will then use that setting as long as characterEncoding has not been set in the connection string.
 This is equivalent to autodetection of the character set.
 ```