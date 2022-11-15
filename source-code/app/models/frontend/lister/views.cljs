
(ns app.models.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :models.lister])]
          [common/item-lister-download-info :models.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [lister-id item-dex {:keys [modified-at name thumbnail types]}]
  (let [timestamp  @(r/subscribe [:activities/get-actual-timestamp modified-at])
        type-count  (count types)
        description {:content :n-items :replacements [type-count]}]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-thumbnail    lister-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell lister-id item-dex {:label name :stretch? true :placeholder :unnamed-model :description description}]
                                            [common/list-item-detail       lister-id item-dex {:content timestamp :width "160px"}]
                                            [common/list-item-marker       lister-id item-dex {:icon :navigate_next}]]}]))

(defn- model-item
  [lister-id item-dex {:keys [id] :as model-item}]
  [elements/toggle {:content     [model-item-structure lister-id item-dex model-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/models/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'model-item :items items}])

(defn- model-lister-body
  []
  [item-lister/body :models.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:models :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'model-list}])

(defn- model-lister-header
  []
  [common/item-lister-header :models.lister
                             {:cells [[common/item-lister-header-spacer :models.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :models.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :models.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :models.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :models.lister
                              {:body   #'model-lister-body
                               :header #'model-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.lister])
        create-model-uri (str "/@app-home/models/create")]
       [common/item-lister-create-item-button :models.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-model-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.lister])]
       [common/item-lister-search-field :models.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-models
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.lister])]
       [common/item-lister-search-description :models.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.lister])]
       [common/surface-breadcrumbs :models.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :models}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.lister])]
       [common/surface-label :models.lister/view
                             {:disabled? lister-disabled?
                              :label     :models}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :models.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :models.lister {}]))

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
