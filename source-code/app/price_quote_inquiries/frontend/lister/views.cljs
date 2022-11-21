
(ns app.price-quote-inquiries.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.item-lister.api     :as item-lister]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :price-quote-inquiries.lister])]
          [common/item-lister-download-info :price-quote-inquiries.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- inquiry-item-structure
  [lister-id item-dex {:keys [modified-at name issuer-logo]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])]
       [common/list-item-structure {:cells [[ {:thumbnail (:media/uri issuer-logo)}]
                                            [common/list-item-label     {:content name :stretch? true :placeholder :unnamed-price-quote-inquiry}]
                                            [common/list-item-detail    {:content timestamp :width "160px"}]
                                            [components/list-item-marker    {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- inquiry-item
  [lister-id item-dex {:keys [id] :as price-quote-inquiry-item}]
  [elements/toggle {:content     [inquiry-item-structure lister-id item-dex inquiry-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/price-quote-inquiries/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- inquiry-list
  []
  (let [items @(r/subscribe [:item-lister/get-downloaded-items :price-quote-inquiries.lister])]
       [common/item-list :price-quote-inquiries.lister {:item-element #'inquiry-item :items items}]))

(defn- inquiry-lister-body
  []
  [item-lister/body :price-quote-inquiries.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:price-quote-inquiries :lister/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [common/item-lister-ghost-element]
                     :list-element     [inquiry-list]}])

(defn- inquiry-lister-header
  []
  [common/item-lister-header :price-quote-inquiries.lister
                             {:cells [[common/item-lister-header-spacer :price-quote-inquiries.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :price-quote-inquiries.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :price-quote-inquiries.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :price-quote-inquiries.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :price-quote-inquiries.lister
                              {:body   #'inquiry-lister-body
                               :header #'inquiry-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-inquiries.lister])
        create-price-quote-inquiry-uri (str "/@app-home/price-quote-inquiries/create")]
       [common/item-lister-create-item-button :price-quote-inquiries.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-price-quote-inquiry-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-inquiries.lister])]
       [common/item-lister-search-field :price-quote-inquiries.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-price-quote-inquiries
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-inquiries.lister])]
       [common/item-lister-search-description :price-quote-inquiries.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-inquiries.lister])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home :route "/@app-home"}
                                                 {:label :price-quote-inquiries}]
                                        :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-inquiries.lister])]
       [components/surface-label ::label
                                 {:disabled? lister-disabled?
                                  :label     :price-quote-inquiries}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :price-quote-inquiries.lister])]
          [:<> [label]
               [breadcrumbs]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :price-quote-inquiries.lister {}]))

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
