
(ns templates.price-quotes.blank.template
    (:require [hiccup.page                             :refer [html5]]
              [hiccup.api                       :as hiccup]
              [templates.price-quotes.blank.cover-page :as cover-page]
              [templates.price-quotes.blank.info-page  :as info-page]
              [templates.price-quotes.blank.main-page  :as main-page]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (map) template-props
  [template-props]
  [:body (cover-page/view template-props)
         (main-page/view  template-props)
         (info-page/view  template-props)])

(defn- head
  ; @param (map) template-props
  [_]
  [:head [:meta {:charset "utf-8"}]
         [:link {:type "text/css" :href "public/css/normalize.css"                        :rel "stylesheet"}]
         [:link {:type "text/css" :href "public/app/css/templates/price-quotes/blank.css" :rel "stylesheet"}]])

(defn- html
  ; @param (map) template-props
  ;  {}
  [{:keys [language] :as template-props}]
  (html5 {:lang language}
         (hiccup/unparse-css (head template-props))
         (hiccup/unparse-css (body template-props))))

(defn view
  ; @param (map) template-props
  ;  {:body (map)
  ;    {:description (hiccup or string)(opt)
  ;     :subtitle (hiccup or string)(opt)
  ;     :title (hiccup or string)(opt)}
  ;   :cover (map)
  ;    {:image (string)}
  ;   :customer (map)
  ;    {:address (hiccup or string)
  ;     :name (hiccup or string)
  ;     :vat-no (hiccup or string)}
  ;   :footer (map)
  ;    {:content (hiccup or string)(opt)}
  ;   :informations (map)
  ;    {:content (hiccup or string)}
  ;   :issuer (map)
  ;    {:details (hiccup or strings in vector)
  ;     :name (hiccup or string)}
  ;     :logo (hiccup or string)(opt)}
  ;   :items (maps in vector)
  ;    [{:product-description (hiccup or string)
  ;      :quantity (hiccup or string)
  ;      :unit-price (hiccup or string)}]
  ;   :language (keyword)
  ;    Default: :en
  ;   :price-quote
  ;    {:name (hiccup or string)
  ;     :release-date (hiccup or string)
  ;     :validity-date (hiccup or string)}}
  ;
  ; @usage
  ;  (template {:issuer {:details ["My detail #1" "My detail #2"]
  ;                      :name    "My Issuer"}})
  [template-props]
  (let [template-props (merge {:language :en} template-props)]
       (html template-props)))
