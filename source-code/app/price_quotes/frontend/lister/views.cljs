
(ns app.price-quotes.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :price-quotes.lister])]
          [common/item-lister-download-info :price-quotes.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-item-structure
  [lister-id item-dex {:keys [modified-at name thumbnail]}]
  (let [timestamp       @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?      @(r/subscribe [:item-lister/item-last? lister-id item-dex])
        price-quote-name {:content :price-quote-n :replacements [name]}]
       [common/list-item-structure {:cells [[common/list-item-thumbnail {:icon :request_quote :icon-family :material-icons-outlined}]
                                            [common/list-item-label     {:content price-quote-name :stretch? true :placeholder :unnamed-price-quote}]
                                            [common/list-item-detail    {:content timestamp :width "160px"}]
                                            [common/list-item-marker    {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- price-quote-item
  [lister-id item-dex {:keys [id] :as price-quote-item}]
  [elements/toggle {:content     [price-quote-item-structure lister-id item-dex price-quote-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/price-quotes/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'price-quote-item :items items}])

(defn- price-quote-lister-body
  []
  [item-lister/body :price-quotes.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:price-quotes :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'price-quote-list}])

(defn- price-quote-lister-header
  []
  [common/item-lister-header :price-quotes.lister
                             {:cells [[common/item-lister-header-spacer :price-quotes.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :price-quotes.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :price-quotes.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :price-quotes.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :price-quotes.lister
                              {:body   #'price-quote-lister-body
                               :header #'price-quote-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled?      @(r/subscribe [:item-lister/lister-disabled? :price-quotes.lister])
        create-price-quote-uri (str "/@app-home/price-quotes/create")]
       [common/item-lister-create-item-button :price-quotes.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-price-quote-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quotes.lister])]
       [common/item-lister-search-field :price-quotes.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-price-quotes
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quotes.lister])]
       [common/item-lister-search-description :price-quotes.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quotes.lister])]
       [common/surface-breadcrumbs :price-quotes.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :price-quotes}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quotes.lister])]
       [common/surface-label :price-quotes.lister/view
                             {:disabled? lister-disabled?
                              :label     :price-quotes}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :price-quotes.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :price-quotes.lister {}]))

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
