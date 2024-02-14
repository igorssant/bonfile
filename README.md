<header>

# Bonfile *(.bon)* Project
<hr>
</header>
<br>
<nav>

## *Table of contents*

- [Short Explanation](#short-explanation)
- [Overview](#overview)
- [Classes]()
- [Methods]()
- [Usage]()
- [Technologies Used](#technologies)
- [Dependencies](#dependencies)
<hr>
</nav>
<br>
<main>
<section>

## Short explanation
The Bonfile Project is a personal project that aims to create an Object notation file format, such as JSON and XML.

The idea came up during the university web and database classes where we (students) would have our first contact with an Object notation file.

At the start it came up as a challenge but eventually grew as a self-learning opportunity.

It aims to mimic the functionality of a JSON/XML file.

Bonfile aims to work with Objects, Array-like data structures, Lists (Java's LinkedList),
Primitive data types (Integer, String, Double, Boolean, etc.), Tuples ranging from unit to sextets
and a HashMap implementation of a Dictionary type i.e.:
```txt
dictionaryExample::dict = {
    one     : 1,
    two     : 2,
    three   : 3,
    four    : 4
};
```
<hr>
</section>
<br>
<section>

## Overview

### Primitive types may be written with or without a variable name
```txt
// An Integer value without a variable name
2500;

// An Integer value with a variable name
integerValue::int = 2500;

// An Float value without a variable name
-11.2;

// An Float value with a variable name
floatValue::float = 7.2;

// An Double value without a variable name
0.701922058108063810;

// An Double value with a variable name
doubleValue::double = -2.5425252524524564645363;

// An Boolean value without a variable name
true;

// An Boolean value with a variable name
booelanValue::bool = false;
```
### Any other type must have a variable name linked to it
#### *Objects might have any data type assigned to them*
```text
johnDoe::Person = {
    age::int = 35;
    height::float = 1.78; // in meters
    weight::float = 85.6; // in kilos
};

integerArray::int = [
    1,
    2,
    3,
    4
];

countries::dict = {
    CANADA : AMERICA,
    PORTUGAL : EUROPE,
    SOUTH_AFRICA : AFRICA,
    BRAZIL : AMERICA,
    CHINA : ASIA
};

tupleExample::tuple = ("some string", 'Z', 11, false, -0.3);
```
Every ***Array*** and ***List*** must be of a single declared data type and their structure is made of `[` and `]` surrounding its values which are separated by a comma and a new line.

Every entry in a ***Dictionary*** is treated as a String and their structure is made of `{` and `}` surrounding its body. The keys are separated from the values by a `:` and each new line contains a pair key-value with a comma in the end of the line.

***Tuples*** might store any kind of data type with no declaration and their structure is made of `(` and `)` surrounding its values which are separated by a comma and a space.

`//` are sigle line commentaries.

`/* */` are multiline commentaries. `/*` opens the commentary and `*/` closes it.
<hr>
</section>
<br>
<section>

## Classes

The classes are not completely implemented yet.
<hr>
</section>
<br>
<section>

## Methods

The methods are not completely implemented yet.
<hr>
</section>
<br>
<section>

## Usage

TODO
<hr>
</section>
<br>
<section>

## Technologies

- MVC project pattern;
- Singleton;
  - Only one instance of a class will te active at a time
- Mediator.
<hr>
</section>
</main>
<br>
<footer>

## Dependencies

- Java version 11 or newer.
</footer>