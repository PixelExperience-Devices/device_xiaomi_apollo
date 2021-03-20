/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.os.ServiceManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.os.Parcel;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import org.lineageos.settings.RefreshRateHandler;

public class RefreshRate extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new RefreshRateFragment())
                .commit();
    }

    public static class RefreshRateFragment extends PreferenceFragment {
        private static final String KEY_REFRESH_RATE = "pref_refresh_rate";
        private ListPreference mPrefRefreshRate;

        IBinder surfaceFlinger = ServiceManager.getService("SurfaceFlinger");

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.refresh_rate);
            mPrefRefreshRate = (ListPreference) findPreference(KEY_REFRESH_RATE);
            mPrefRefreshRate.setOnPreferenceChangeListener(PrefListener);
        }

        @Override
        public void onResume() {
            super.onResume();
            updateValuesAndSummaries();
        }

        private void updateValuesAndSummaries() {
            mPrefRefreshRate.setSummary(mPrefRefreshRate.getValue());
        }

        private Preference.OnPreferenceChangeListener PrefListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    final String key = preference.getKey();

                    if (KEY_REFRESH_RATE.equals(key)) {
                        RefreshRateHandler.setFPS((int) Integer.parseInt((String) value));
			RefreshRateHandler.setRefreshRate(getActivity(), Integer.parseInt((String) value));
                    }

                    updateValuesAndSummaries();
                    return true;
                }
            };

    }
}
