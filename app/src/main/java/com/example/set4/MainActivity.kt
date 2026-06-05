package com.example.set4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.set4.ui.theme.Set4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Set4Theme {
                Scaffold { innerPadding ->
                    RentalScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RentalScreen(modifier: Modifier = Modifier) {
    val rentalText = remember {
        val rental = Rental(5)
        rental.addVehicle(Car("Honda Civic", CombustionVehicle.DIESEL or CombustionVehicle.PETROL))
        rental.addVehicle(Motorboat("Super motorboat", 0))
        rental.addVehicle(Car("Mercedes CLK", CombustionVehicle.DIESEL or CombustionVehicle.LPG))
        rental.addVehicle(Bicycle("Giant"))
        rental.addVehicle(Scooter("Cool scooter"))
        rental.parkVehicleInGarage(0, 1)
        rental.printAllVehicles()
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Vehicle Rental System")
        Text(text = "")
        Text(text = rentalText)
    }
}

@Preview(showBackground = true)
@Composable
fun RentalScreenPreview() {
    Set4Theme {
        RentalScreen()
    }
}
