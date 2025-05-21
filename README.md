# МиниРаст (хотя уже хз) Компилятор

### Запуск

Консольное приложение для запуска различных визиторов.

Из корня проекта выполнить:
- Компиляция
```
.\gradlew.bat :Compiler:shadowJar
```
- Запуск
```
java -jar .\Compiler\build\libs\MiniRustCompiler.jar <arguments>
```

Аргументы для запуска:

- ```-ast inputFileName outputFileName``` - visitor, печатающий AST в dot формате.
- ```-run inputFileName``` - visitor-интерпретатор.
- ```-ir inputFileName outputFileName``` - IR-visitor.
- ```-scope inputFileName outputFileName``` - scope-visitor, выводит граф из таблиц символов в различных скоупах.
- ```-typecheck inputFileName``` - type-check-visitor, проверяет типы в программе и выводит ошибки типизации.
- ```-compile inputFileName outputFileName [optimizationLevel]``` - компилирует программу в исполняемый файл. optimizationLevel - уровень оптимизации (0-3, по умолчанию 0).

Пример запуска:

windows
```
java -jar .\Compiler\build\libs\MiniRustCompiler.jar -compile example\program.txt program.exe
.\program.exe
```

linux
```
java -jar ./Compiler/build/libs/MiniRustCompiler.jar -compile example/program.txt program
./program
```

### Компиляция в исполняемый файл

Для компиляции программы в исполняемый файл используется опция `-compile`. Процесс компиляции состоит из следующих шагов:

1. Генерация IR-кода из исходного файла
2. Преобразование IR-кода в стандартный LLVM IR с указанием target triple
3. Применение оптимизаций LLVM в зависимости от указанного уровня оптимизации
4. Компиляция LLVM IR в исполняемый файл с использованием LLVM/Clang API

#### Реализация

Компилятор определяет target triple системы и передает его в LLVM IR. 
Также поддерживаются различные уровни оптимизации (0-3). 
Для генерации исполняемого файла используется следующий процесс:

1. На Windows: попытка использовать Microsoft C++ компилятор (cl.exe), затем LLVM LLC и линковщик
2. На macOS: использование clang
3. На Linux: использование gcc

Для работы компилятора требуется наличие одного из поддерживаемых компиляторов в системе (cl.exe, llc+link, clang или gcc).

### Тесты

```angular2html
./gradlew test
```
