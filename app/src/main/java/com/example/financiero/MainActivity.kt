package com.example.financiero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialApp()
        }
    }
}

// ViewModel para manejar el estado
class FinancialViewModel : ViewModel() {
    val categories = listOf(
        "Cálculos de Productos",
        "Cálculos de Empleador",
        "Cálculos de Empleado"
    )
}

// Pantalla principal con navegación
@Composable
fun FinancialApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "categoryList") {
        composable("categoryList") {
            CategoryScreen(navController)
        }
        composable("categoryDetail/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            if (categoryName == "Cálculos de Productos") {
                ProductCalculationsScreen()
            } else if (categoryName == "Cálculos de Empleador") {
                EmployerCalculationsScreen()
            } else if (categoryName == "Cálculos de Empleado") {
                EmployeeCalculationsScreen()
            } else {
                CategoryDetailScreen(categoryName ?: "Sin nombre")
            }
        }
    }
}

// Pantalla de lista de categorías
@Composable
fun CategoryScreen(navController: NavHostController, viewModel: FinancialViewModel = viewModel()) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
                .background(Color(0xFF557CE4)))

                {
                // Título
                Text(
                    text = "FinanciApp",
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Black
                )
                // Lista de categorías
                CategoryList(viewModel.categories) { category ->
                    navController.navigate("categoryDetail/$category")
                }
            }
        }
    }
}

// Detalle de categoría
@Composable
fun CategoryDetailScreen(categoryName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Detalles de: $categoryName",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Pantalla de Cálculos de Productos
@Composable
fun ProductCalculationsScreen() {
    var productName by remember { mutableStateOf("") }
    var basePrice by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var fixedCosts by remember { mutableStateOf("") }
    var variableCost by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Resultados aparecerán aquí") }
    val productList = remember { mutableStateListOf<String>() }

    // Función para calcular los resultados
    fun calculate() {
        try {
            val basePriceValue = basePrice.toDouble()
            val costValue = cost.toDouble()
            val fixedCostsValue = fixedCosts.toDouble()
            val variableCostValue = variableCost.toDouble()
            val investmentValue = investment.toDouble()

            // Fórmulas de cálculo
            val priceWithIVA = basePriceValue * 1.19
            val marginOfProfit = ((priceWithIVA - costValue) / priceWithIVA) * 100
            val breakEvenPoint = fixedCostsValue / (priceWithIVA - variableCostValue)
            val roi = ((priceWithIVA - investmentValue) / investmentValue) * 100

            // Guardar el resultado en el texto
            resultText = """
                Producto: $productName
                Precio con IVA: ${"%.2f".format(priceWithIVA)}
                Margen de ganancia: ${"%.2f".format(marginOfProfit)}%
                Punto de equilibrio: ${"%.2f".format(breakEvenPoint)}
                ROI: ${"%.2f".format(roi)}%
            """.trimIndent()

            // Guardar el resultado en la lista de productos
            productList.add(resultText)

        } catch (e: Exception) {
            resultText = "Error en los cálculos. Verifique los valores ingresados."
        }
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cálculos de Productos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de entrada: Nombre del producto
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Precio base del producto
        TextField(
            value = basePrice,
            onValueChange = { basePrice = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Precio base del producto") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Costo del producto
        TextField(
            value = cost,
            onValueChange = { cost = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Costo del producto") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Costos fijos
        TextField(
            value = fixedCosts,
            onValueChange = { fixedCosts = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Costos fijos") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Costo variable unitario
        TextField(
            value = variableCost,
            onValueChange = { variableCost = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Costo variable unitario") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Inversión inicial
        TextField(
            value = investment,
            onValueChange = { investment = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Inversión inicial") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de cálculo
        Button(onClick = { calculate() }) {
            Text(text = "Calcular")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de productos calculados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = "Historial de productos calculados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(productList) { product ->
                        Text(
                            text = product,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


// Pantalla de Cálculos de Empleador
@Composable
fun EmployerCalculationsScreen() {
    var salary by remember { mutableStateOf("") }
    var benefits by remember { mutableStateOf("") }
    var employerResult by remember { mutableStateOf("") }
    val totalCostList = remember { mutableStateListOf<String>() }

    fun calculate() {
        try {
            val salaryValue = salary.toDouble()
            val benefitsValue = benefits.toDoubleOrNull() ?: 0.0

            // Cálculos individuales
            val parafiscales = salaryValue * 0.09
            val socialProvisions = salaryValue * 0.205
            val socialBenefits = salaryValue * 0.2183

            // Costo total de nómina
            val totalCost = salaryValue + parafiscales + socialProvisions + socialBenefits + benefitsValue

            // Mostrar resultados
            employerResult = """
                Para un salario base : ${"%.2f".format(salaryValue)}
                Costo total de nómina: ${"%.2f".format(totalCost)}
                Aportes parafiscales: ${"%.2f".format(parafiscales)}
                Seguridad social: ${"%.2f".format(socialProvisions)}
                Prestaciones sociales: ${"%.2f".format(socialBenefits)}
            """.trimIndent()

            // Guardar resultado en la lista
            //totalCostList.add("Costo total: ${"%.2f".format(totalCost)}")
            totalCostList.add(employerResult)

        } catch (e: Exception) {
            employerResult = "Error en los cálculos. Verifique los valores ingresados."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cálculos de Empleador",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de entrada: Salario base
        TextField(
            value = salary,
            onValueChange = { salary = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Salario mensual del empleado") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada: Beneficios adicionales
        TextField(
            value = benefits,
            onValueChange = { benefits = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Beneficios adicionales (opcional)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de cálculo
        Button(onClick = { calculate() }) {
            Text(text = "Calcular")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Box con lista de resultados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = "Historial de cálculos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(totalCostList) { totalCost ->
                        Text(
                            text = totalCost,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}



// Pantalla de Cálculos de Empleado
@Composable
fun EmployeeCalculationsScreen() {
    var idEmployee  by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }
    var hoursWorkedDay by remember { mutableStateOf("") }
    var hoursWorkedNight by remember { mutableStateOf("") }
    var hoursWorkedSunday by remember { mutableStateOf("") }
    var employeeResult by remember { mutableStateOf("Resultados aparecerán aquí") }

    //Lista en la que mostrare los resultados calculados
    val resultsList = remember { mutableStateListOf<Pair<String, String>>() }

    fun calculate() {
        try {
            val hourlyRateValue = hourlyRate.toDouble()
            val hoursWorkedDayValue = hoursWorkedDay.toDouble()
            val hoursWorkedNightValue = hoursWorkedNight.toDouble()
            val hoursWorkedSundayValue = hoursWorkedSunday.toDouble()

            // Cálculos de deducciones
            val pensionDeduction = hourlyRateValue * 0.04
            val healthDeduction = hourlyRateValue * 0.04
            val totalDeductions = pensionDeduction + healthDeduction

            // Cálculo del salario neto
            val netSalary = hourlyRateValue - totalDeductions

            // Cálculo de horas extras
            val hourlyRatePerHour = hourlyRateValue / 240
            val extraDayPay = hourlyRatePerHour * 1.25
            val extraNightPay = hourlyRatePerHour * 1.75
            val extraSundayPay = hourlyRatePerHour * 2

            // Cálculo de salario por horas extras
            val totalExtraDay = extraDayPay * hoursWorkedDayValue
            val totalExtraNight = extraNightPay * hoursWorkedNightValue
            val totalExtraSunday = extraSundayPay * hoursWorkedSundayValue

            // Cálculo total del salario
            val totalSalary = netSalary + totalExtraDay + totalExtraNight + totalExtraSunday

            // Mostrar resultado
            employeeResult = "Salario total del empleado: ${"%.2f".format(totalSalary)}"

            // Se agrega el resultado a la lista
            resultsList.add(idEmployee to "%.2f".format(totalSalary))
        } catch (e: Exception) {
            employeeResult = "Error en los cálculos. Verifique los valores ingresados."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cálculos de Empleado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        //id empleado
        TextField(
            value = idEmployee,
            onValueChange = { idEmployee = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Id Empleado") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Salario base
        TextField(
            value = hourlyRate,
            onValueChange = { hourlyRate = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Salario Base") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = hoursWorkedDay,
            onValueChange = { hoursWorkedDay = it.filter { char -> char.isDigit() } },
            label = { Text("Horas extras Diurnas") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = hoursWorkedNight,
            onValueChange = { hoursWorkedNight = it.filter { char -> char.isDigit() } },
            label = { Text("Horas extras Nocturnas") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = hoursWorkedSunday,
            onValueChange = { hoursWorkedSunday = it.filter { char -> char.isDigit() } },
            label = { Text("Horas extras Dominical") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { calculate() }) {
            Text(text = "Calcular")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar resultados
        TextField(
            value = employeeResult,
            onValueChange = {},
            label = { Text("Resultado") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Mostrar lista de resultados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            LazyColumn {
                items(resultsList) { result ->
                    Text(
                        text = "Id Empleado: ${result.first}, Salario: ${result.second}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// Composable para mostrar la lista de categorías
@Composable
fun CategoryList(categories: List<String>, onCategoryClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        categories.forEach { category ->
            CategoryItem(category) { onCategoryClick(category) }
        }
    }
}

// Composable para cada ítem de categoría
@Composable
fun CategoryItem(category: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = category,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinancialApp()
}
