package com.ls.dagger_library;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;


import dagger.android.AndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.internal.Beta;

import static android.util.Log.DEBUG;
import static dagger.internal.Preconditions.checkNotNull;

/** Injects support Android types. */
@Beta
public final class CustomInjection {

    private static final String TAG = "CustomInjection";

    /**
     * Injects {@code fragment} if an associated {@link AndroidInjector} implementation can be found,
     * otherwise throws an {@link IllegalArgumentException}.
     *
     * <p>Uses the following algorithm to find the appropriate {@link AndroidInjector} to use to
     * inject {@code fragment}:
     *
     * <ol>
     *   <li>Walks the parent-fragment hierarchy to find a fragment that implements {@link
     *       HasAndroidInjector}, and if none do
     *   <li>Uses the {@code fragment}'s {@link Fragment#getActivity() activity} if it implements
     *       {@link HasAndroidInjector} , and if not
     *   <li>Uses the {@link Application} if it implements {@link HasAndroidInjector}.
     * </ol>
     *
     * If none of them implement {@link HasAndroidInjector}, a {@link
     * IllegalArgumentException} is thrown.
     *
     * @throws IllegalArgumentException if no parent fragment, activity, or application implements
     *     {@link HasAndroidInjector}.
     */
    public static void inject(Fragment fragment) {
        checkNotNull(fragment, "fragment");

        Object hasInjector = findHasFragmentInjector(fragment);
        AndroidInjector<? super Fragment> injector;
        if (hasInjector instanceof HasAndroidInjector) {
            injector = ((HasAndroidInjector) hasInjector).androidInjector();
            checkNotNull(injector, "%s.androidInjector() returned null", hasInjector.getClass());
        } else {
            throw new RuntimeException(
                    String.format(
                            "%s does not implement %s or %s",
                            hasInjector.getClass().getCanonicalName(),
                            HasAndroidInjector.class.getCanonicalName()));
        }

        if (Log.isLoggable(TAG, DEBUG)) {
            Log.d(
                    TAG,
                    String.format(
                            "An injector for %s was found in %s",
                            fragment.getClass().getCanonicalName(),
                            hasInjector.getClass().getCanonicalName()));
        }

        injector.inject(fragment);
    }

    private static Object findHasFragmentInjector(Fragment fragment) {
        Fragment parentFragment = fragment;
        while ((parentFragment = parentFragment.getParentFragment()) != null) {
            if (parentFragment instanceof HasAndroidInjector) {
                return parentFragment;
            }
        }
        Activity activity = fragment.getActivity();
        if (activity instanceof HasAndroidInjector) {
            return activity;
        }
        Application application = activity.getApplication();
        if (application instanceof HasAndroidInjector) {
            return application;
        }
        throw new IllegalArgumentException(
                String.format("No injector was found for %s", fragment.getClass().getCanonicalName()));
    }

    public static void inject(InjectPager pager) {
        checkNotNull(pager, "pager");

        Object hasInjector = findHasPagerInjector(pager);
        AndroidInjector<? super InjectPager> injector;
        if (hasInjector instanceof HasAndroidInjector) {
            injector = ((HasAndroidInjector) hasInjector).androidInjector();
            checkNotNull(injector, "%s.androidInjector() returned null", hasInjector.getClass());
        } else {
            throw new RuntimeException(
                    String.format(
                            "%s does not implement %s or %s",
                            hasInjector.getClass().getCanonicalName(),
                            HasAndroidInjector.class.getCanonicalName()));
        }

        if (Log.isLoggable(TAG, DEBUG)) {
            Log.d(
                    TAG,
                    String.format(
                            "An injector for %s was found in %s",
                            pager.getClass().getCanonicalName(),
                            hasInjector.getClass().getCanonicalName()));
        }

        injector.inject(pager);
    }

    private static Object findHasPagerInjector(InjectPager pager) {
        Fragment parentFragment = pager.getFragment();
        if(parentFragment != null) {
            if (parentFragment instanceof HasAndroidInjector) {
                return parentFragment;
            }
        }

        Activity activity = pager.getActivity();
        if(activity != null) {
            if (activity instanceof HasAndroidInjector) {
                return activity;
            }
            Application application = activity.getApplication();
            if (application instanceof HasAndroidInjector) {
                return application;
            }
        }
        throw new IllegalArgumentException(
                String.format("No injector was found for %s", pager.getClass().getCanonicalName()));
    }


    private CustomInjection(){}
}
