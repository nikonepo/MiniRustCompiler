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

### Тесты

```angular2html
./gradlew test
```
