package com.helpsetu.app.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.HelpSetuBackground
import com.example.ui.theme.HelpSetuGreen
import com.example.ui.theme.HelpSetuGreenDark
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.helpsetu.app.presentation.worker.WorkerViewModel

@Composable
fun LoginScreen(
    viewModel: WorkerViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phoneInput by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("CUSTOMER") } // "CUSTOMER" or "WORKER"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HelpSetuBackground)
            .padding(24.dp)
            .testTag("login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Branding Logo
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(HelpSetuGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "HelpSetu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900,
            letterSpacing = (-0.5).sp
        )

        Text(
            text = "Direct Local Services / सीधा स्थानीय संपर्क",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Slate500
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Role Selector: Customer vs Service Provider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate100, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val isCustomer = selectedRole == "CUSTOMER"
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { selectedRole = "CUSTOMER" }
                    .testTag("role_customer_button"),
                color = if (isCustomer) Color.White else Color.Transparent,
                border = if (isCustomer) BorderStroke(1.dp, Slate200) else null,
                shadowElevation = if (isCustomer) 1.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = if (isCustomer) HelpSetuGreen else Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Customer / ग्राहक",
                        fontSize = 13.sp,
                        fontWeight = if (isCustomer) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCustomer) Slate900 else Slate600
                    )
                }
            }

            val isWorker = selectedRole == "WORKER"
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { selectedRole = "WORKER" }
                    .testTag("role_worker_button"),
                color = if (isWorker) Color.White else Color.Transparent,
                border = if (isWorker) BorderStroke(1.dp, Slate200) else null,
                shadowElevation = if (isWorker) 1.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Handyman,
                        contentDescription = null,
                        tint = if (isWorker) HelpSetuGreen else Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Worker / कामगार",
                        fontSize = 13.sp,
                        fontWeight = if (isWorker) FontWeight.Bold else FontWeight.Medium,
                        color = if (isWorker) Slate900 else Slate600
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Phone Input Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate100),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = if (selectedRole == "CUSTOMER")
                        "Enter your mobile number to view & contact local workers"
                    else
                        "Enter mobile number to manage your service provider profile",
                    fontSize = 13.sp,
                    color = Slate500,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Mobile Number") },
                    placeholder = { Text("9876543210") },
                    prefix = { Text("+91 ", fontWeight = FontWeight.Bold, color = HelpSetuGreenDark) },
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = Slate400)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_phone_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuGreen,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val entered = if (phoneInput.isNotBlank()) "+91$phoneInput" else "+919876501234"
                        viewModel.setCustomerPhone(entered)
                        viewModel.setUserRole(selectedRole)
                        onContinue()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("login_submit_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HelpSetuGreen)
                ) {
                    Text(
                        text = if (selectedRole == "CUSTOMER") "Continue to Services" else "Continue as Worker",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Value props
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LoginBenefitItem(text = "Direct call & WhatsApp to electricians & plumbers")
            LoginBenefitItem(text = "Zero middlemen commissions & transparent charges")
            LoginBenefitItem(text = "Nearby verified workers within 1-3 KM range")
        }
    }
}

@Composable
fun LoginBenefitItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Emerald50),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = HelpSetuGreen,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Slate600
        )
    }
}
