
(ns templates.price-quotes.blank.main-page
    (:require [css.api                            :as css]
              [re-frame.api                       :as r]
              [server-fruits.base64               :as base64]
              [templates.price-quotes.blank.items :as items]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @param (map) template-props
  ;  {}
  [{{:keys [content]} :footer}]
  [:div {:id :blk-main-page--footer}
        (letfn [(f [result dex line] (case dex 0 (conj result       line)
                                                 (conj result [:br] line)))]
               (if (coll? content)
                   (reduce-kv f [:h6 {:style {:color "#888"}}] content)))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- items
  ; @param (map) template-props
  [template-props]
  (items/view template-props 0 10))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-description
  ; @param (map) template-props
  ;  {}
  [{{:keys [description]} :body}]
  [:div {:id :blk-main-page--body-description}
        [:h5 {:style {:color "#666"}} description]])

(defn- body-subtitle
  ; @param (map) template-props
  ;  {}
  [{{:keys [subtitle]} :body}]
  [:div {:id :blk-main-page--body-subtitle}
        [:h5 {:style {:color "#666"}} subtitle]])

(defn- body-title
  ; @param (map) template-props
  ;  {}
  [{{:keys [title]} :body}]
  [:div {:id :blk-main-page--body-title}
        [:h5 [:strong title]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- customer
  ; @param (map) template-props
  [{:keys [language] {:keys [address name vat-no]} :customer}]
  [:table [:tbody [:tr [:td [:h5 {:style {:color "#666"}} @(r/subscribe [:dictionary/look-up :name    {:language language}])]]
                       [:td [:h5 name]]]
                  [:tr [:td [:h5 {:style {:color "#666"}} @(r/subscribe [:dictionary/look-up :address {:language language}])]]
                       [:td [:h5 address]]]
                  [:tr [:td [:h5 {:style {:color "#666"}} @(r/subscribe [:dictionary/look-up :vat-no  {:language language}])]]
                       [:td [:h5 vat-no]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- validity
  ; @param (map) template-props
  [{:keys [language] {:keys [release-date validity-date]} :price-quote}]
  [:table [:tbody [:tr [:td [:h5 {:style {:color "#666"}} @(r/subscribe [:dictionary/look-up :released    {:language language}])]]
                       [:td [:h5 release-date]]]
                  [:tr [:td [:h5 {:style {:color "#666"}} @(r/subscribe [:dictionary/look-up :valid-until {:language language}])]]
                       [:td [:h5 validity-date]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- customer-and-validity
  ; @param (map) template-props
  ;  {}
  [{:keys [language] {:keys [name]} :price-quote :as template-props}]
  (let [label @(r/subscribe [:dictionary/look-up :price-quote {:language language}])]
       [:table [:tbody [:tr [:td {:id :blk-main-page--customer}
                                 [:h5 [:strong label]]
                                 (customer template-props)]
                            [:td {:id :blk-main-page--validity}
                                 [:h5 [:strong name]]
                                 (validity template-props)]]]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-name
  ; @param (map) template-props
  ;  {}
  [{{:keys [name]} :price-quote}]
  [:div {:id :blk-main-page--price-quote-name}
        [:h1 name]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- issuer-logo
  ; @param (map) template-props
  ;  {:issuer (map)
  ;   {:logo (string)}}
  [{{:keys [logo]} :issuer}]
  (let [base64 (base64/encode logo "environment/temp/templates.price-quotes.blank.header-logo.b64")]
       [:img#blk-main-page--issuer-logo {:src (str "data:image/png;base64,"base64)}]))

(defn- issuer-name
  ; @param (map) template-props
  ;  {}
  [{{:keys [name]} :issuer}]
  [:h1 name])

(defn- issuer-details
  ; @param (map) template-props
  ;  {}
  [{{:keys [details]} :issuer}]
  (letfn [(f [result dex line] (case dex 0 (conj result       line)
                                           (conj result [:br] line)))]
         (if (coll? details)
             (reduce-kv f [:h5 {:style {:color "#666"}}] details))))

(defn- issuer
  ; @param (map) template-props
  [template-props]
  [:table {:id :blk-main-page--issuer}
          [:tbody [:tr [:td (issuer-name    template-props)
                            (issuer-details template-props)]
                       [:td (issuer-logo    template-props)]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) template-props
  [template-props]
  [:div {:id :blk-main-page}
        (issuer                template-props)
       ;(price-quote-name      template-props)
        (customer-and-validity template-props)
        (body-title            template-props)
        (body-subtitle         template-props)
        (items                 template-props)
        (body-description      template-props)
        (footer                template-props)])
