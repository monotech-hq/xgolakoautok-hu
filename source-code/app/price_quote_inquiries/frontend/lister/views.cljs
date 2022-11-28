
(ns app.price-quote-inquiries.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :price-quote-inquiries.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- inquiry-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) inquiry-item
  [_ _ item-dex {:keys [added-at id issuer-logo]}]
  ; XXX#7703
  ; Az árajánlat-igénylések a többi adattól eltérően nem módosíthatók, így a megjelenítésükkor
  ; a módosítás ideje helyett a létrehozás idejét szükséges megjeleníteni!
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp added-at])]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          [components/list-item-thumbnail {:thumbnail (:media/uri issuer-logo)}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-price-quote-inquiry}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-button    {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/price-quote-inquiries/"id)]}]
                                          [components/list-item-gap       {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- inquiry-list-header
  []
  ; XXX#7703
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :price-quote-inquiries.lister])]
       [components/item-list-header ::inquiry-list-header
                                    {:cells [{:width 12}
                                             {:width 84}
                                             {:width 12}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :price-quote-inquiries.lister :name]}
                                             {:width 12}
                                             {:label :created :width 100 :order-by-key :added-at
                                              :on-click [:item-lister/order-items! :price-quote-inquiries.lister :added-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- inquiry-lister
  []
  [common/item-lister-body :price-quote-inquiries.lister
                           {:list-item-element #'inquiry-list-item
                            :item-list-header  #'inquiry-list-header
                            :items-path        [:price-quote-inquiries :lister/downloaded-items]}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [inquiry-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :price-quote-inquiries.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :price-quote-inquiries}]
                              :on-search [:item-lister/search-items! :price-quote-inquiries.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-price-quote-inquiries
                              :label              :price-quote-inquiries}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
