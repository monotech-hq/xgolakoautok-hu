
(ns app.price-quotes.frontend.viewer.views
    (:require [app.common.frontend.api                :as common]
              [app.components.frontend.api            :as components]
              [app.price-quotes.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                           :as elements]
              [layouts.surface-a.api                  :as surface-a]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :price-quotes.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-overview
  []
  [:<> [viewer.boxes/price-quote-template-box]
       [viewer.boxes/price-quote-client-box]
       [viewer.boxes/price-quote-prices-box]
       [viewer.boxes/price-quote-model-box]
       [viewer.boxes/price-quote-type-box]
       [viewer.boxes/price-quote-products-box]
       [viewer.boxes/price-quote-services-box]
       [viewer.boxes/price-quote-packages-box]
       [viewer.boxes/price-quote-validity-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quotes.viewer])]
       (case current-view-id :overview [price-quote-overview])))

(defn- body
  []
  [common/item-viewer-body :price-quotes.viewer
                           {:item-element [view-selector]
                            :item-path    [:price-quotes :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [quote-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        quote-name @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :name]])
        edit-route  (str "/@app-home/price-quotes/"quote-id"/edit")]
       [common/item-viewer-header :price-quotes.viewer
                                  {:label       quote-name
                                   :placeholder :unnamed-price-quote
                                   :crumbs     [{:label :app-home     :route "/@app-home"}
                                                {:label :price-quotes :route "/@app-home/price-quotes"}
                                                {:label quote-name :placeholder :unnamed-price-quote}]
                                   :menu-items [{:label :overview}
                                                {:label :preview :disabled? true}]
                                   :on-edit    [:x.router/go-to! edit-route]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
