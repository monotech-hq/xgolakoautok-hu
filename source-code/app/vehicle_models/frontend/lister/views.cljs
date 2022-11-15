
(ns app.vehicle-models.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :vehicle-models.lister])]
          [common/item-lister-download-info :vehicle-models.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [lister-id item-dex {:keys [modified-at name thumbnail types]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])
        type-count {:content :n-items :replacements [(count types)]}]
       [common/list-item-structure {:cells [[common/list-item-thumbnail    {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell {:label name :stretch? true :placeholder :unnamed-vehicle-model :description type-count}]
                                            [common/list-item-detail       {:content timestamp :width "160px"}]
                                            [common/list-item-marker       {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- model-item
  [lister-id item-dex {:keys [id] :as model-item}]
  [elements/toggle {:content     [model-item-structure lister-id item-dex model-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/vehicle-models/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'model-item :items items}])

(defn- model-lister-body
  []
  [item-lister/body :vehicle-models.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:vehicle-models :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'model-list}])

(defn- model-lister-header
  []
  [common/item-lister-header :vehicle-models.lister
                             {:cells [[common/item-lister-header-spacer :vehicle-models.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :vehicle-models.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :vehicle-models.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :vehicle-models.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :vehicle-models.lister
                              {:body   #'model-lister-body
                               :header #'model-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.lister])
        create-model-uri (str "/@app-home/vehicle-models/create")]
       [common/item-lister-create-item-button :vehicle-models.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-model-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.lister])]
       [common/item-lister-search-field :vehicle-models.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-vehicle-models
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.lister])]
       [common/item-lister-search-description :vehicle-models.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.lister])]
       [common/surface-breadcrumbs :vehicle-models.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :vehicle-models}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.lister])]
       [common/surface-label :vehicle-models.lister/view
                             {:disabled? lister-disabled?
                              :label     :vehicle-models}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :vehicle-models.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :vehicle-models.lister {}]))

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
