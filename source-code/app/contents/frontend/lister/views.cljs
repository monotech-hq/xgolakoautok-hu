
(ns app.contents.frontend.lister.views
    (:require [app.common.frontend.api               :as common]
              [app.components.frontend.api           :as components]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [elements.api                          :as elements]
              [hiccup.api                            :as hiccup]
              [layouts.surface-a.api                 :as surface-a]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :contents.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) content-item
  [_ _ item-dex {:keys [body id modified-at name]}]
  (let [timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        content-body (-> body handler.helpers/parse-content-body hiccup/to-string)]
       [components/item-list-row {:cells [[components/list-item-thumbnail {:icon :article :icon-family :material-icons-outlined}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-content}
                                                                                  {:content content-body :placeholder "n/a" :color :muted :font-size :xs}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-button    {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/contents/"id)]}]
                                          [components/list-item-gap       {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-list-header
  []
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :contents.lister])]
       [components/item-list-header ::content-list-header
                                    {:cells [{:width 84}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :contents.lister :name]}
                                             {:width 12}
                                             {:label :modified :width 100 :order-by-key :modified-at
                                              :on-click [:item-lister/order-items! :contents.lister :modified-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- content-lister
  []
  [common/item-lister-body :contents.lister
                           {:list-item-element #'content-list-item
                            :item-list-header  #'content-list-header
                            :items-path        [:contents :lister/downloaded-items]}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [content-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :contents.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :contents}]
                              :on-create [:x.router/go-to! "/@app-home/contents/create"]
                              :on-search [:item-lister/search-items! :contents.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-contents
                              :label              :contents}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
