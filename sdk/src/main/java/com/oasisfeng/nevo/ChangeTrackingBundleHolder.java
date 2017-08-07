/*
 * Copyright (C) 2015 The Nevolution Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oasisfeng.nevo;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

import com.oasisfeng.android.os.BundleHolder;
import com.oasisfeng.android.os.IBundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Bundle holder with change tracking capability.
 *
 * Created by Oasis on 2015/9/10.
 */
@RestrictTo(LIBRARY_GROUP)
public class ChangeTrackingBundleHolder extends BundleHolder {

	int countChanges() {
		return mChangedKeys.size();		// No need to synchronize for ArrayList
	}

	public Set<String> getChangedKeys() {
		synchronized (mChangedKeys) {
			return new HashSet<>(mChangedKeys);
		}
	}

	protected void onChanged(final String key, final Object value) {
		synchronized (mChangedKeys) {
			mChangedKeys.add(key);
		}
	}

	@Override public IBundle getBundle(final String key) {
		final Bundle bundle = local.getBundle(key);
		return bundle != null ? new ChangeTrackingBundleHolder(bundle) : null;
	}

	public ChangeTrackingBundleHolder(final Bundle bundle) { super(bundle); }

	private final List<String> mChangedKeys = new ArrayList<>();
}