package com.adevinta.android.barista.sample

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDrawableDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertListItemCount
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.adevinta.android.barista.internal.failurehandler.BaristaException
import junit.framework.AssertionFailedError
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class ListViewAssertionTest {

  @Rule
  @JvmField
  var activityTestRule = ActivityTestRule(ListsActivity::class.java, true, false)

  @Test
  fun shouldHaveExpectedNumberOfEntriesInListView() {
    openSimpleListActivity()
    val expectedListLength = ListsActivity.FRUITS.size
    assertListItemCount(R.id.listview, expectedListLength)
  }

  @Test(expected = BaristaException::class)
  fun shouldFailWhenNumberOfEntriesInListViewDoesNotMatchExpected() {
    openSimpleListActivity()
    val expectedListLength = ListsActivity.FRUITS.size
    assertListItemCount(R.id.listview, expectedListLength + 1)
  }

  @Test
  fun shouldHaveEntriesInListView() {
    openSimpleListActivity()
    assertListNotEmpty(R.id.listview)
  }

  @Test
  fun shouldFindItemInListViewWithoutIdInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 2, text = "Avocado")
  }

  @Test
  fun shouldFindItemInListViewWithIdInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 4, targetViewId = android.R.id.text1, text = "Bilberry")
  }

  @Test(expected = AssertionFailedError::class)
  fun shouldFailWhenUnableToFindItemInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 2, text = "NotThere")
  }

  @Test
  fun shouldFindItemInListViewWithoutIdInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 86, text = "Tamarind")
  }

  @Test
  fun shouldFindItemInListViewWithIdInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 19, targetViewId = R.id.textview, text = "Dragonfruit")
  }

  @Test(expected = AssertionFailedError::class)
  fun shouldFailWhenUnableToFindItemInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 86, text = "Missing")
  }

  @Test
  fun shouldFindItemByIdInListViewWithoutIdInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 2, textId = R.string.avocado)
  }

  @Test
  fun shouldFindByIdItemInListViewWithIdInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 4, targetViewId = android.R.id.text1, textId = R.string.bilberry)
  }

  @Test(expected = AssertionFailedError::class)
  fun shouldFailWhenUnableToFindItemByIdInSimpleList() {
    openSimpleListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 2, textId = R.string.not_there)
  }

  @Test
  fun shouldFindItemByIdInListViewWithoutIdInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 86, textId = R.string.tamarind)
  }

  @Test
  fun shouldFindItemByIdInListViewWithIdInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 19, targetViewId = R.id.textview, textId = R.string.dragonfruit)
  }

  @Test(expected = AssertionFailedError::class)
  fun shouldFailWhenUnableToFindItemByIdInComplexList() {
    openComplexListActivity()
    assertDisplayedAtPosition(listId = R.id.listview, position = 86, textId = R.string.missing)
  }

  @Test
  fun shouldFindItemInListViewWithCustomAssertionInSimpleList() {
    openSimpleListActivity()
    assertCustomAssertionAtPosition(
        listId = R.id.listview,
        position = 4,
        targetViewId = android.R.id.text1,
        viewAssertion = ViewAssertions.matches(
            CoreMatchers.anyOf(
                ViewMatchers.withChild(ViewMatchers.withText("Bilberry")),
                ViewMatchers.withText("Bilberry")
            )
        )
    )
  }

  @Test
  fun shouldFindItemInListViewWithCustomAssertionInComplexList() {
    openComplexListActivity()
    assertCustomAssertionAtPosition(
        listId = R.id.listview,
        position = 19,
        targetViewId = R.id.textview,
        viewAssertion = ViewAssertions.matches(
            CoreMatchers.anyOf(
                    ViewMatchers.withChild(ViewMatchers.withText("Dragonfruit")),
                    ViewMatchers.withText("Dragonfruit")
            )
        )
    )
  }

  @Test
  fun shouldFindItemInListViewWithSpecificDrawableWithoutIdInComplexList() {
    openComplexListActivity()
    assertDrawableDisplayedAtPosition(
        listId = R.id.listview,
        position = 0,
        drawableRes = R.drawable.ic_barista
    )
  }

  @Test
  fun shouldFindItemInListViewWithSpecificDrawableWithIdInComplexList() {
    openComplexListActivity()
    assertDrawableDisplayedAtPosition(
        listId = R.id.listview,
        position = 1,
        targetViewId = R.id.imageview,
        drawableRes = R.drawable.ic_barista
    )
  }

  @Test(expected = AssertionFailedError::class)
  fun shouldFailWhenUnableToFindItemInListViewWithSpecificDrawable() {
    openComplexListActivity()
    assertDrawableDisplayedAtPosition(
            listId = R.id.listview,
            position = 4,
            targetViewId = R.id.imageview,
            drawableRes = R.drawable.ic_barista
    )
  }

  private fun openActivity(intentBuilder: ListsActivity.IntentBuilder) {
    activityTestRule.launchActivity(intentBuilder.build(ApplicationProvider.getApplicationContext()))
  }

  private fun openSimpleListActivity() {
    openActivity(ListsActivity.buildIntent().withSimpleLists(R.id.listview))
  }

  private fun openComplexListActivity() {
    openActivity(ListsActivity.buildIntent().withComplexLists(R.id.listview))
  }
}