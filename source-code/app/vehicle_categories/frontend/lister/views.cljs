
(ns app.vehicle-categories.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.item-lister.api     :as item-lister]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :vehicle-categories.lister])]
          [common/item-lister-download-info :vehicle-categories.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-item-structure
  [item-dex {:keys [models modified-at name thumbnail]} {:keys [handle-attributes]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? :vehicle-categories.lister item-dex])
        model-count {:content :n-items :replacements [(count models)]}]
       [common/list-item-structure {:cells [[components/list-item-drag-handle {:indent {:left :xs} :drag-attributes handle-attributes}]
                                            [components/list-item-thumbnail   {:thumbnail (:media/uri thumbnail)}]
                                            [components/list-item-cell        {:rows [{:content name :placeholder :unnamed-vehicle-category}
                                                                                      {:content model-count :font-size :xs :color :muted}]
                                                                               :width :stretch}]
                                            [components/list-item-cell        {:rows [{:content timestamp :font-size :xs :color :muted}] :width "160px"}]
                                            [components/list-item-marker      {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- category-item
  [item-dex {:keys [id] :as category-item} {:keys [dragging? item-attributes] :as drag-props}]
  [:div item-attributes
        [elements/toggle {:background-color (if dragging? :highlight)
                          :content          [category-item-structure item-dex category-item drag-props]
                          :hover-color      :highlight
                          :on-click         [:x.router/go-to! (str "/@app-home/vehicle-categories/"id)]}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-list
  []
  (let [items @(r/subscribe [:item-lister/get-downloaded-items :vehicle-categories.lister])]
       [dnd-kit/body :vehicle-categories.lister
                     {:items            items
                      :item-id-f        :id
                      :item-element     #'category-item
                      :on-order-changed (fn [_ _ %] (r/dispatch-sync [:item-lister/reorder-items! :vehicle-categories.lister %]))}]))

(defn- category-lister-body
  []
  [item-lister/body :vehicle-categories.lister
                    {:default-order-by :order/descending
                     :items-path       [:vehicle-categories :lister/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [common/item-lister-ghost-element]
                     :list-element     [category-list]}])

(defn- category-lister-header
  []
  [common/item-lister-header :vehicle-categories.lister
                             {:cells [[common/item-lister-header-spacer :vehicle-categories.lister {:width "144px"}]
                                      [common/item-lister-header-cell   :vehicle-categories.lister {:label :name :stretch? true}]
                                      [common/item-lister-header-cell   :vehicle-categories.lister {:label :last-modified :width "160px"}]
                                      [common/item-lister-header-spacer :vehicle-categories.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :vehicle-categories.lister
                              {:body   #'category-lister-body
                               :header #'category-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled?   @(r/subscribe [:item-lister/lister-disabled? :vehicle-categories.lister])
        create-category-uri (str "/@app-home/vehicle-categories/create")]
       [common/item-lister-create-item-button :vehicle-categories.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-category-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-categories.lister])]
       [common/item-lister-search-field :vehicle-categories.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-vehicle-categories
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-categories.lister])]
       [common/item-lister-search-description :vehicle-categories.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-categories.lister])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home :route "/@app-home"}
                                                 {:label :vehicle-categories}]
                                        :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-categories.lister])]
       [components/surface-label ::label
                                 {:disabled? lister-disabled?
                                  :label     :vehicle-categories}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :vehicle-categories.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :vehicle-categories.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  ; @param (keyword) surface-id
  [_]
  [:<> [header]
       [body]
       [footer]])

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [:div "halika"])
  ;; [surface-a/layout surface-id
  ;;                   {:content #'view-structure}])
