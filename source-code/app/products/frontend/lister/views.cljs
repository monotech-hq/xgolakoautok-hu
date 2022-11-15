
(ns app.products.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :products.lister])]
          [common/item-lister-download-info :products.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-item-structure
  [lister-id item-dex {:keys [item-number modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-thumbnail lister-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-label     lister-id item-dex {:content name :stretch? true}]
                                            [common/list-item-detail    lister-id item-dex {:content item-number :width "160px"}]
                                            [common/list-item-detail    lister-id item-dex {:content timestamp :width "160px"}]
                                            [common/list-item-marker    lister-id item-dex {:icon :navigate_next}]]}]))

(defn- product-item
  [lister-id item-dex {:keys [id] :as product-item}]
  [elements/toggle {:content     [product-item-structure lister-id item-dex product-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/products/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'product-item :items items}])

(defn- product-lister-body
  []
  [item-lister/body :products.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:products :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'product-list}])

(defn- product-lister-header
  []
  [common/item-lister-header :products.lister
                             {:cells [[common/item-lister-header-spacer :products.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :products.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :products.lister {:label :item-number   :order-by-key :item-number :width "160px"}]
                                      [common/item-lister-header-cell   :products.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :products.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :products.lister
                              {:body   #'product-lister-body
                               :header #'product-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.lister])
        create-product-uri (str "/@app-home/products/create")]
       [common/item-lister-create-item-button :products.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-product-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.lister])]
       [common/item-lister-search-field :products.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-products
                                         :search-keys [:item-number :name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.lister])]
       [common/item-lister-search-description :products.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.lister])]
       [common/surface-breadcrumbs :products.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :products}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.lister])]
       [common/surface-label :products.lister/view
                             {:disabled? lister-disabled?
                              :label     :products}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :products.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :products.lister {}]))

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
