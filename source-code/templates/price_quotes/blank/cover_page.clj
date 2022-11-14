
(ns templates.price-quotes.blank.cover-page
    (:require [css.api       :as css]
              [server-fruits.base64 :as base64]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; @param (map) template-props
  [{{:keys [image]} :cover}]
  (let [base64 (try (base64/encode image "environment/temp/templates.price-quotes.blank.cover-image.b64")
                    (catch Exception e nil))]
       [:div#blk-cover-page--image {:style {:background-image (css/url (str "data:image/png;base64,"base64))}}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-data
  ; @param (map) template-props
  [template-props]
  [:div {:id :blk-cover-page--price-quote-data}
        [:h1 "#2022/0002-1"]
        [:table [:tbody [:tr [:td [:h5 {:style {:color "#666"}} "Kiadva"]]
                             [:td [:h5 "2022-10-15"]]]
                        [:tr [:td [:h5 {:style {:color "#666"}} "Érvényesség"]]
                             [:td [:h5 "2023-01-13"]]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- issuer-data
  ; @param (map) template-props
  [template-props]
  [:div {:id :blk-cover-page--issuer-data}
        [:h1 "Árajánlat"]
        [:table [:tbody [:tr] ;[:td [:h5 {:style {:color "#666"}} "Kiadva"]]
                             ;[:td [:h5 "Perlina Kft."]]]
                        [:tr]]]]) ;[:td [:h5 {:style {:color "#666"}} "Érvényesség"]]]]]])
                             ;[:td [:h5 "2023-01-13"]]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) template-props
  [template-props]
  [:div {:id :blk-cover-page}
        (issuer-data      template-props)
        (price-quote-data template-props)
        (image            template-props)])
