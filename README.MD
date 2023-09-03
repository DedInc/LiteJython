# LiteJython: Minimalist Python Environment Setup Utility

LiteJython is a simple tool for managing Python environments. It's a lightweight utility written in Java for downloading, extracting, and preparing a Python environment for execution.

## How to Use

The `LiteJython` class has a zero-argument constructor that prepares the Python environment upon instantiation.

```java
LiteJython liteJython = new LiteJython();
```

### Execute Python Scripts

You can execute Python scripts by calling the method `executePythonScript` on a `LiteJython` instance. This method takes the path to the Python script as a string.

```java
liteJython.executePythonScript("path/to/your/script.py");
```

### Execute Pip Commands

To execute pip commands, call the `executePip` method on a `LiteJython` instance. This method takes the pip command to be executed as a string.

```java
liteJython.executePip("install numpy");
```

## Error Handling

If there is any issue during the setup or execution of Python scripts or pip commands, the utility will throw a relevant exception with an appropriate message.

## Requirements

This utility is intended for use on Windows operating systems. It detects the OS type and architecture (32 or 64 bit) to download the appropriate Python environment setup.

## API Documentation

The JavaDoc comments in the `LiteJython` class provide more detailed information about each method and its usage.

## Contributions

We welcome contributions from the open-source community. Please feel free to open issues or submit pull requests.

## License

LiteJython is open-source software licensed under [LICENSE](LICENSE).