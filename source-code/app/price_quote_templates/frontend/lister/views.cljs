
(ns app.price-quote-templates.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :price-quote-templates.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) template-item
  [_ _ item-dex {:keys [id issuer-logo modified-at name]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          [components/list-item-thumbnail {:thumbnail (:media/uri issuer-logo)}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-price-quote-template}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-button    {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/price-quote-templates/"id)]}]
                                          [components/list-item-gap       {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list-header
  []
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :price-quote-templates.lister])]
       [components/item-list-header ::template-list-header
                                    {:cells [{:width 12}
                                             {:width 84}
                                             {:width 12}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :price-quote-templates.lister :name]}
                                             {:width 12}
                                             {:label :modified :width 100 :order-by-key :modified-at
                                              :on-click [:item-lister/order-items! :price-quote-templates.lister :modified-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- template-lister
  []
  [common/item-lister-body :price-quote-templates.lister
                           {:list-item-element #'template-list-item
                            :item-list-header  #'template-list-header
                            :items-path        [:price-quote-templates :lister/downloaded-items]}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [template-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :price-quote-templates.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :price-quote-templates}]
                              :on-create [:x.router/go-to! "/@app-home/price-quote-templates/create"]
                              :on-search [:item-lister/search-items! :price-quote-templates.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-price-quote-templates
                              :label              :price-quote-templates}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
