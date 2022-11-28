
(ns app.vehicle-categories.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :vehicle-categories.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) category-item
  [_ _ item-dex {:keys [id modified-at name thumbnail]} {:keys [handle-attributes item-attributes]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
       [components/item-list-row {:cells [[components/list-item-gap         {:width 12}]
                                          [components/list-item-drag-handle {:indent {:left :xs} :drag-attributes handle-attributes}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-thumbnail   {:thumbnail (:media/uri thumbnail)}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-cell        {:rows [{:content name :placeholder :unnamed-vehicle-category}]}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-cell        {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-button      {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/vehicle-categories/"id)]}]
                                          [components/list-item-gap         {:width 12}]]
                                  :border (if (not= item-dex 0) :top)
                                  :drag-attributes item-attributes}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-list-header
  []
  [components/item-list-header ::category-list-header
                               {:cells [{:width 12}
                                        {:width 24}
                                        {:width 12}
                                        {:width 84}
                                        {:width 12}
                                        {:label :name}
                                        {:width 12}
                                        {:label :modified :width 100}
                                        {:width 12}
                                        {:width 100}
                                        {:width 12}]
                                :border :bottom}])

(defn- category-lister
  []
  [common/item-lister-body :vehicle-categories.lister
                           {:default-order-by  :order/ascending
                            :list-item-element #'category-list-item
                            :item-list-header  #'category-list-header
                            :items-path        [:vehicle-categories :lister/downloaded-items]
                            :on-order-changed  [:item-lister/reorder-items! :vehicle-categories.lister]
                            :sortable?         true}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [category-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :vehicle-categories.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :vehicle-categories}]
                              :on-create [:x.router/go-to! "/@app-home/vehicle-categories/create"]
                              :on-search [:item-lister/search-items! :vehicle-categories.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-vehicle-categories
                              :label              :vehicle-categories}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------


(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
