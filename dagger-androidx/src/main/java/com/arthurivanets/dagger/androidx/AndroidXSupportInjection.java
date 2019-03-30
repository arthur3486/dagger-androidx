package com.arthurivanets.dagger.androidx;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.internal.Beta;

import static android.util.Log.DEBUG;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Injects core Android types from support libraries (Adapted to AndroidX).
 */
@Beta
public final class AndroidXSupportInjection {


    private static final String TAG = "dagger.android.support";




    private AndroidXSupportInjection() {
        //
    }




    /**
     * Injects {@code fragment} if an associated {@link dagger.android.AndroidInjector} implementation
     * can be found, otherwise throws an {@link IllegalArgumentException}.
     *
     * <p>Uses the following algorithm to find the appropriate {@code AndroidInjector<Fragment>} to
     * use to inject {@code fragment}:
     *
     * <ol>
     *   <li>Walks the parent-fragment hierarchy to find the a fragment that implements {@link
     *       AndroidXHasSupportFragmentInjector}, and if none do
     *   <li>Uses the {@code fragment}'s {@link Fragment#getActivity() activity} if it implements
     *       {@link AndroidXHasSupportFragmentInjector}, and if not
     *   <li>Uses the {@link android.app.Application} if it implements {@link
     *       AndroidXHasSupportFragmentInjector}.
     * </ol>
     *
     * If none of them implement {@link AndroidXHasSupportFragmentInjector}, a {@link
     * IllegalArgumentException} is thrown.
     *
     * @throws IllegalArgumentException if no parent fragment, activity, or application implements
     *     {@link AndroidXHasSupportFragmentInjector}.
     */
    public static void inject(Fragment fragment) {
        checkNotNull(fragment, "fragment");

        final AndroidXHasSupportFragmentInjector hasSupportFragmentInjector = findHasFragmentInjector(fragment);

        if(Log.isLoggable(TAG, DEBUG)) {
            Log.d(
                TAG,
                String.format(
                    "An injector for %s was found in %s",
                    fragment.getClass().getCanonicalName(),
                    hasSupportFragmentInjector.getClass().getCanonicalName()
                )
            );
        }

        final AndroidInjector<Fragment> fragmentInjector = hasSupportFragmentInjector.supportFragmentInjector();

        checkNotNull(
            fragmentInjector,
            "%s.supportFragmentInjector() returned null",
            hasSupportFragmentInjector.getClass()
        );

        fragmentInjector.inject(fragment);
    }




    private static AndroidXHasSupportFragmentInjector findHasFragmentInjector(Fragment fragment) {
        Fragment parentFragment = fragment;

        while((parentFragment = parentFragment.getParentFragment()) != null) {
            if(parentFragment instanceof AndroidXHasSupportFragmentInjector) {
                return (AndroidXHasSupportFragmentInjector) parentFragment;
            }
        }

        final Activity activity = fragment.getActivity();

        if(activity instanceof AndroidXHasSupportFragmentInjector) {
            return (AndroidXHasSupportFragmentInjector) activity;
        }

        if(activity.getApplication() instanceof AndroidXHasSupportFragmentInjector) {
            return (AndroidXHasSupportFragmentInjector) activity.getApplication();
        }

        throw new IllegalArgumentException(String.format(
            "No injector was found for %s",
            fragment.getClass().getCanonicalName()
        ));
    }




}