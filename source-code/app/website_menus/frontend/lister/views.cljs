
(ns app.website-menus.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [keyword.api                 :as keyword]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :website-menus.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) menu-item
  [_ _ item-dex {:keys [id modified-at name]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
       [components/item-list-row {:cells [[components/list-item-thumbnail {:icon :article :icon-family :material-icons-outlined}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-website-menu}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-button    {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/website-menus/"id)]}]
                                          [components/list-item-gap       {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-list-header
  []
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :website-menus.lister])]
       [components/item-list-header ::menu-list-header
                                    {:cells [{:width 84}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :website-menus.lister :name]}
                                             {:width 12}
                                             {:label :modified :width 100 :order-by-key :modified-at
                                              :on-click [:item-lister/order-items! :website-menus.lister :modified-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- menu-lister
  []
  [common/item-lister-body :website-menus.lister
                           {:list-item-element #'menu-list-item
                            :item-list-header  #'menu-list-header
                            :items-path        [:website-menus :lister/downloaded-items]}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [menu-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :website-menus.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :website-menus}]
                              :on-create [:x.router/go-to! "/@app-home/website-menus/create"]
                              :on-search [:item-lister/search-items! :website-menus.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-website-menus
                              :label              :website-menus}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
