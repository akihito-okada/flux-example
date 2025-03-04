package com.example.flux.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkProductionYearsType : Parcelable {
    The2020s,
    The2010s,
    The2000s,
    The1990s,
    The1980s,
    The1970s,
    The1960s,
    The1950s,
    BeforeThe1940s,
    Unknown,
    ;

    val isUnknown get() = this == Unknown

    val value
        get() = when (this) {
            The2020s -> "2020s"
            The2010s -> "2010s"
            The2000s -> "2000s"
            The1990s -> "1990s"
            The1980s -> "1980s"
            The1970s -> "1970s"
            The1960s -> "1960s"
            The1950s -> "1950s"
            BeforeThe1940s -> "Before1940s"
            Unknown -> ""
        }

    val labelRes
        @StringRes
        get() = when (this) {
            The2020s -> R.string.years_the_2020s
            The2010s -> R.string.years_the_2010s
            The2000s -> R.string.years_the_2000s
            The1990s -> R.string.years_the_1990s
            The1980s -> R.string.years_the_1980s
            The1970s -> R.string.years_the_1970s
            The1960s -> R.string.years_the_1960s
            The1950s -> R.string.years_the_1950s
            BeforeThe1940s -> R.string.years_before_the1940s
            Unknown -> R.string.blank
        }

    val searchConditions
        get() = when (this) {
            The2020s -> listOf(2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029)
            The2010s -> listOf(2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019)
            The2000s -> listOf(2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009)
            The1990s -> listOf(1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999)
            The1980s -> listOf(1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989)
            The1970s -> listOf(1970, 1971, 1972, 1973, 1974, 1975, 1976, 1977, 1978, 1979)
            The1960s -> listOf(1960, 1961, 1962, 1963, 1964, 1965, 1966, 1967, 1968, 1969)
            The1950s -> listOf(1950, 1951, 1952, 1953, 1954, 1955, 1956, 1957, 1958, 1959)
            BeforeThe1940s ->
                listOf(
                    1940, 1941, 1942, 1943, 1944, 1945, 1946, 1947, 1948, 1949,
                    1930, 1931, 1932, 1933, 1934, 1935, 1936, 1937, 1938, 1939,
                    1920, 1921, 1922, 1923, 1924, 1925, 1926, 1927, 1928, 1929,
                    1910, 1911, 1912, 1913, 1914, 1915, 1916, 1917, 1918, 1919,
                    1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909,
                )

            Unknown -> listOf()
        }

    companion object {
        val items = entries.filter { it.isUnknown.not() }
    }
}
