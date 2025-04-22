# МиниРаст (хотя уже хз) Компилятор

### Запуск

Консольное приложение для запуска различных визиторов.

Из корня проекта выполнить:

```
./gradlew :Compiler:run --args="-<mode> <args>
```

Аргументы для запуска:

- ```-ast inputFileName outputFileName``` - visitor, печатающий AST в dot формате.
- ```-run inputFileName``` - visitor-интерпретатор.
- ```-ir inputFileName outputFileName``` - IR-visitor.
- ```-scope inputFileName outputFileName``` - scope-visitor, выводит граф из таблиц символов в различных скоупах.

### Тесты

```angular2html
./gradlew test   
```