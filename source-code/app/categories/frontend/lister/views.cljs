
(ns app.categories.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :categories.lister])]
          [common/item-lister-download-info :categories.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-item-structure
  [lister-id item-dex {:keys [models modified-at name thumbnail]} dnd-kit-props]
  (let [timestamp  @(r/subscribe [:activities/get-actual-timestamp modified-at])
        model-count (count models)
        description {:content :n-items :replacements [model-count]}]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-drag-handle  lister-id item-dex dnd-kit-props]
                                            [common/list-item-thumbnail    lister-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell lister-id item-dex {:label name :stretch? true :placeholder :unnamed-category :description description}]
                                            [common/list-item-detail       lister-id item-dex {:content timestamp :width "160px"}]
                                            [common/list-item-marker       lister-id item-dex {:icon :navigate_next}]]}]))

(defn- category-item
  [lister-id item-dex {:keys [id] :as category-item} {:keys [isDragging] :as dnd-kit-props}]
  [elements/toggle {:background-color (if isDragging :highlight)
                    :content          [category-item-structure lister-id item-dex category-item dnd-kit-props]
                    :hover-color      :highlight
                    :on-click         [:router/go-to! (str "/@app-home/categories/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-list
  [_ items]
  [dnd-kit/body :categories.lister
                {:items            items
                 :item-id-f        :id
                 :item-element     #'category-item
                 :on-order-changed [:item-lister/reorder-items! :categories.lister]}])

(defn- category-lister-body
  []
  [item-lister/body :categories.lister
                    {:default-order-by :order/descending
                     :items-path       [:categories :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'category-list}])

(defn- category-lister-header
  []
  [common/item-lister-header :categories.lister
                             {:cells [[common/item-lister-header-spacer :categories.lister {:width "144px"}]
                                      [common/item-lister-header-cell   :categories.lister {:label :name :stretch? true}]
                                      [common/item-lister-header-cell   :categories.lister {:label :last-modified :width "160px"}]
                                      [common/item-lister-header-spacer :categories.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :categories.lister
                              {:body   #'category-lister-body
                               :header #'category-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled?   @(r/subscribe [:item-lister/lister-disabled? :categories.lister])
        create-category-uri (str "/@app-home/categories/create")]
       [common/item-lister-create-item-button :categories.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-category-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :categories.lister])]
       [common/item-lister-search-field :categories.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-categories
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :categories.lister])]
       [common/item-lister-search-description :categories.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :categories.lister])]
       [common/surface-breadcrumbs :categories.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :categories}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :categories.lister])]
       [common/surface-label :categories.lister/view
                             {:disabled? lister-disabled?
                              :label     :categories}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :categories.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :categories.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
