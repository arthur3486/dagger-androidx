package com.arthurivanets.dagger.androidx;

import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.internal.Beta;

/**
 * Provides an {@link AndroidInjector} of {@link Fragment}s. (Adapted to AndroidX)
 */
@Beta
public interface AndroidXHasSupportFragmentInjector {

    /**
     * Returns an {@link AndroidInjector} of {@link Fragment}s.
     */
    AndroidInjector<Fragment> supportFragmentInjector();

}
