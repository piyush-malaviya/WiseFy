package com.isupatches.wisefy

import android.os.Build

import com.isupatches.wisefy.WiseFy.Companion.WIFI_MANAGER_FAILURE
import com.isupatches.wisefy.callbacks.AddNetworkCallbacks
import com.isupatches.wisefy.constants.MISSING_PARAMETER
import com.isupatches.wisefy.internal.base.BaseUnitTest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.mock
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tests the ability to add WEP networks.
 *
 * @author Patches
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M])
@Suppress("deprecation")
internal class AddWEPNetworkTests : BaseUnitTest() {

    @Test fun sync_failure_prechecks() {
        mockWiseFyPrechecksUtil.addNetwork_failure()
        assertEquals(MISSING_PARAMETER, wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD))
        verificationUtil.didNoTryToAddNetwork()
    }

    @Test fun sync_failure() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_failure()
        assertEquals(WIFI_MANAGER_FAILURE, wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD))
        verificationUtil.triedToAddNetwork()
    }

    @Test fun sync_success() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_success()
        assertNotEquals(1, wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD))
        verificationUtil.triedToAddNetwork()
    }

    @Test fun async_failure_prechecks() {
        mockWiseFyPrechecksUtil.addNetwork_failure()
        val mockCallbacks = mock(AddNetworkCallbacks::class.java)
        wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD, mockCallbacks)
        verify(mockCallbacks, timeout(VERIFICATION_SUCCESS_TIMEOUT)).wisefyFailure(MISSING_PARAMETER)
        verificationUtil.didNoTryToAddNetwork()
    }

    @Test fun async_failure_prechecks_nullCallback() {
        mockWiseFyPrechecksUtil.addNetwork_failure()
        nullCallbackUtil.callAddWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD)
        verificationUtil.didNoTryToAddNetwork()
    }

    @Test fun async_failure() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_failure()
        val mockCallbacks = mock(AddNetworkCallbacks::class.java)
        wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD, mockCallbacks)
        verify(mockCallbacks, timeout(VERIFICATION_SUCCESS_TIMEOUT)).failureAddingNetwork(WIFI_MANAGER_FAILURE)
        verificationUtil.triedToAddNetwork()
    }

    @Test fun async_failure_nullCallback() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_failure()
        nullCallbackUtil.callAddWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD)
        verificationUtil.triedToAddNetwork()
    }

    @Test fun async_success() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_success()
        val mockCallbacks = mock(AddNetworkCallbacks::class.java)
        wisefy.addWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD, mockCallbacks)
        verifyNetworkAdded(mockCallbacks)
        verificationUtil.triedToAddNetwork()
    }

    @Test fun async_success_nullCallback() {
        mockWiseFyPrechecksUtil.addNetwork_success()
        mockNetworkUtil.addNetwork_success()
        nullCallbackUtil.callAddWEPNetwork(WEP_NETWORK_SSID, WEP_NETWORK_PASSWORD)
        verificationUtil.triedToAddNetwork()
    }
}
