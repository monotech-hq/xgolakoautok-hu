
(ns app.contents.frontend.lister.views
    (:require [app.common.frontend.api               :as common]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [elements.api                          :as elements]
              [engines.item-lister.api               :as item-lister]
              [layouts.surface-a.api                 :as surface-a]
              [hiccup.api                     :as hiccup]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-item-structure
  [lister-id item-dex {:keys [body modified-at name]}]
  (let [timestamp   @(r/subscribe [:activities/get-actual-timestamp modified-at])
        content-body (-> body handler.helpers/parse-content-body hiccup/to-string)]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-thumbnail-icon lister-id item-dex {:icon :article :icon-family :material-icons-outlined}]
                                            ; HACK#4506
                                            ; A túl hosszú tartalom ne lógjon ki az elemből!
                                            ; Ez a megoldás ideiglenes!
                                            [:div {:style {:flex-grow 1 :max-width "calc(100% - 304px)" :padding-right "24px"}}
                                                  [common/list-item-primary-cell lister-id item-dex {:label name
                                                                                                     :placeholder :unnamed-content
                                                                                                     :description content-body}]]
                                            [common/list-item-detail lister-id item-dex {:content timestamp :width "160px"}]
                                            [common/list-item-marker lister-id item-dex {:icon    :navigate_next}]]}]))

(defn- content-item
  [lister-id item-dex {:keys [id] :as content-item}]
  [elements/toggle {:content     [content-item-structure lister-id item-dex content-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/contents/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'content-item :items items}])

(defn- content-lister-body
  []
  [item-lister/body :contents.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:contents :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'content-list}])

(defn- content-lister-header
  []
  [common/item-lister-header :contents.lister
                             {:cells [[common/item-lister-header-spacer :contents.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :contents.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :contents.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :contents.lister {:width "36px"}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.lister])
        create-content-uri (str "/@app-home/contents/create")]
       [common/item-lister-create-item-button :contents.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-content-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.lister])]
       [common/item-lister-search-field :contents.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-contents
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.lister])]
       [common/item-lister-search-description :contents.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.lister])]
       [common/surface-breadcrumbs :contents.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :contents}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.lister])]
       [common/surface-label :contents.lister/view
                             {:disabled? lister-disabled?
                              :label     :contents}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :contents.lister])]
          [common/item-lister-download-info :contents.lister {}]))

(defn- body
  []
  [common/item-lister-wrapper :contents.lister
                              {:body   #'content-lister-body
                               :header #'content-lister-header}])

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :contents.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :contents.lister {}]))

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
