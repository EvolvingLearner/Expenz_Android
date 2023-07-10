package com.money.expenz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.money.expenz.R
import com.money.expenz.data.DataSource
import com.money.expenz.model.Subscription

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            //val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
            setContent {
                MaterialTheme {
                    TotalIncomeExpenseCard()

                }
            }
        }
    }

    @Composable
    fun SubscriptionList(subscriptions: List<Subscription>, modifier: Modifier = Modifier) {
        LazyRow {
            items(subscriptions) { subscription -> SubscriptionCard(subscription, modifier) }
        }
    }

    @Composable
    fun SubscriptionCard(subscription: Subscription, modifier: Modifier = Modifier) {
        Card(modifier = modifier
            .padding(8.dp)
            .width(180.dp)
            .height(80.dp), elevation = 4.dp) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(subscription.imageResourceId),
                    contentDescription = null,
                    modifier = modifier.padding(5.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(subscription.stringResourceId),
                    modifier = modifier.padding(10.dp),
                    style = MaterialTheme.typography.h2,
                    fontSize = 25.sp
                )
            }
        }
    }

    @Preview
    @Composable
    fun SubscriptionCardPreview() {
        TotalIncomeExpenseCard()
        //SubscriptionCard(Subscription(R.string.netflix, R.drawable.ic_menu_camera))
    }

    @Composable
    fun TotalIncomeExpenseCard(modifier: Modifier = Modifier) {
        Column(/*horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween*/) {
            Box( modifier = modifier.fillMaxWidth().padding(10.dp)) {
                Card(modifier = modifier
                    .padding(15.dp)
                    .height(180.dp)
                    .align(Alignment.CenterStart), elevation = 4.dp) {
                    Text(
                        text = stringResource(id = R.string.total_income),
                        modifier = modifier.padding(10.dp),
                        style = MaterialTheme.typography.h2,
                        fontSize = 20.sp
                    )
                }

                Card(modifier = modifier
                    .padding(15.dp)
                    .height(180.dp)
                    .align(Alignment.CenterEnd), elevation = 4.dp) {
                    Text(
                        text = stringResource(id = R.string.total_Expense),
                        modifier = modifier.padding(10.dp),
                        style = MaterialTheme.typography.h2,
                        fontSize = 20.sp
                    )
                }
            }
            SubscriptionList(subscriptions = DataSource().loadSubscriptions())
        }
    }
}

