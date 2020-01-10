# eBay Product Search - Android

[Jump to screenshots](#screenshots)

* Android application, developed in Android Studio, following Material Design standards.
* Users could search for products on eBay, using eBay's APIs, among others.
* Product's many details could be viewed and they could be added to a wish list.
* Products could also be shared to social media.

This application was built to work alongside my [web application](https://github.com/avikola/ebay-product-search-web), which had similar functionalities, and was deployed using AWS Elastic Beanstalk.

∴ The Android application used the same Node.js/Express backend to make API calls.

## Notable Features/Implementations

### > [eBay Search API](https://developer.ebay.com/DevZone/finding/Concepts/FindingAPIGuide.html)

### > [Google Play Services API](https://developers.google.com/android/guides/setup) for Location Handling

### > [ip-api](https://ip-api.com/) Retrieve User Location Info

### > [GeoNames API](https://www.geonames.org/) to Autocomplete zip code entries

### > RecyclerView for Search Results & Similar Products List

### > Add to Wishlist Button for every product listed

### > Social Media Share Button in Product Details - [FB API](https://developers.facebook.com/docs/sharing/reference/share-dialog)

### > Product Image Gallery

### > Link/Browser Navigation

### > CircularScoreView Library Used for Popularity Display - [Repo Link](https://github.com/wssholmes/CircularScore)

### > [Google Custom Search JSON API](https://developers.google.com/custom-search/v1/overview) for Product Related Images

### > [Picasso](https://square.github.io/picasso/) Image Downloading & Caching Library for Product Related Images

### > Added Filter and Sorting Options for Similar Products View

### > Thorough Error & Edge Case Handling

<br/>

## Screenshots

<div>
<img src="https://github.com/avikola/ebay-product-search-android/blob/master/screenshots/search.png" alt="search" width="250"/>
<img src="https://github.com/avikola/ebay-product-search-android/blob/master/screenshots/search_expanded.png" alt="search expanded" width="250"/>
<img src="https://github.com/avikola/ebay-product-search-android/blob/master/screenshots/wishlist.png" alt="wishlist" width="250"/>
</div>

<br/>
<br/>

***TODO: Get more screenshots of the working application***


### ***Issue: AWS Application Deployment is terminated. ∴ API calls are failing.***

<br/>

**However,**

#### The application was developed to exactly match the following Youtube demo:

**---->** <a target=_blank href="https://www.youtube.com/watch?v=_RpseDGV6I8">eBay Product Search - Android</a>
