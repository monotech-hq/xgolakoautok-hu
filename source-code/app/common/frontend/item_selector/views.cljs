
(ns app.common.frontend.item-selector.views
    (:require [app.common.frontend.item-selector.prototypes :as item-selector.prototypes]
              [app.components.frontend.api                  :as components]
              [elements.api                                 :as elements]
              [engines.item-browser.api                     :as item-browser]
              [engines.item-lister.api                      :as item-lister]
              [re-frame.api                                 :as r]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- all-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  [_ {:keys []}]
  [elements/toggle ::all-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :check_box
                                             :icon-family :material-icons-outlined}]}])

(defn- no-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  [_ {:keys []}]
  [elements/toggle ::no-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :check_box_outline_blank
                                             :icon-family :material-icons-outlined}]}])

(defn- some-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  [_ {:keys []}]
  [elements/toggle ::some-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :indeterminate_check_box
                                             :icon-family :material-icons-outlined}]}])

(defn- handle-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  [selector-id {:keys [all-downloaded-item-selected? any-downloaded-item-selected?] :as footer-props}]
  (cond all-downloaded-item-selected? [all-item-selected-button  selector-id footer-props]
        any-downloaded-item-selected? [some-item-selected-button selector-id footer-props]
        :return                       [no-item-selected-button   selector-id footer-props]))


(defn- discard-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {:on-discard-selection (metamorphic-event)
  ;   :selected-item-count (integer)}
  [_ {:keys [on-discard-selection selected-item-count]}]
  [elements/button ::discard-selection-button
                   {:disabled?     (< selected-item-count 1)
                    :font-size     :xs
                    :icon          :close
                    :icon-position :right
                    :indent        {:horizontal :xxs :vertical :xs}
                    :on-click      on-discard-selection
                    :label {:content :n-items-selected :replacements [selected-item-count]}}])

(defn- item-selector-footer
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {:on-discard (metamorphic-event)
  ;   :selected-item-count (integer)}
  ;
  ; @usage
  ;  [item-selector-footer :my-selector {...}]
  [selector-id footer-props]
  [:div {:style {:display "flex" :justify-content "space-between"}}
        [:div]
        [discard-selection-button selector-id footer-props]])
       ;[handle-selection-button  selector-id footer-props]

;; -- Control-bar components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-items-field
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :search-field-placeholder (metamorphic-content)(opt)
  ;   :search-keys (keywords in vector)}
  [selector-id {:keys [disabled? search-field-placeholder search-keys]}]
  (let [search-event [:item-lister/search-items! selector-id {:search-keys search-keys}]]
       [:div {:style {:flex-grow 1}}
             [elements/search-field ::search-items-field
                                    {:autoclear?    true
                                     :disabled?     disabled?
                                     :indent        {:horizontal :xxs :left :xxs}
                                     :on-empty      search-event
                                     :on-type-ended search-event
                                     :placeholder   search-field-placeholder}]]))

(defn- order-by-icon-button
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :order-by-options (namespaced keywords in vector)}
  [selector-id {:keys [disabled? order-by-options]}]
  [elements/icon-button ::order-by-icon-button
                        {:border-radius :s
                         :disabled?     disabled?
                         :hover-color   :highlight
                         :on-click      [:item-lister/choose-order-by! selector-id {:order-by-options order-by-options}]
                         :preset        :order-by}])

(defn item-selector-control-bar
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :order-by-options (namespaced keywords in vector)
  ;   :search-field-placeholder (metamorphic-content)(opt)
  ;   :search-keys (keywords in vector)}
  ;
  ; @usage
  ;  [item-selector-control-bar :my-selector {...}]
  [selector-id bar-props]
  (let [bar-props (item-selector.prototypes/control-bar-props-prototype selector-id bar-props)]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? selector-id])]
               [elements/row ::item-selector-control-bar
                             {:content [:<> [search-items-field   selector-id bar-props]
                                            [order-by-icon-button selector-id bar-props]]}]
               [elements/horizontal-separator {:height :xxl}])))

;; -- Label-bar components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-selector-label-bar
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:label (metamorphic-content)
  ;   :on-close (metamorphic-event)}
  ;
  ; @usage
  ;  [item-selector-label-bar :my-selector {...}]
  [selector-id {:keys [label on-close]}]
  [components/popup-label-bar ::item-selector-label-bar
                              {:primary-button   {:label :save! :on-click [:item-selector/save-selection! selector-id]}
                               :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? selector-id])]
                                                         {:label :abort!  :on-click [:item-selector/abort-autosave! selector-id]}
                                                         {:label :cancel! :on-click on-close})
                               :label label}])

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-body
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;  {:list-item-element (component or symbol)}
  [selector-id {:keys [list-item-element]}]
  (let [downloaded-items @(r/subscribe [:item-lister/get-downloaded-items selector-id])
        selector-props   @(r/subscribe [:x.db/get-item [:engines :engine-handler/meta-items selector-id]])]
       (letfn [(f [item-list item-dex item]
                  (conj item-list [list-item-element selector-id selector-props item-dex item]))]
              (reduce-kv f [:<>] downloaded-items))))

(defn- item-list
  ; @param (keyword) selector-id
  ; @param (map) body-props
  [selector-id body-props]
  ; BUG#7610
  ; A {width: 100%} tulajdonságú table elem popup elemen megjelenítve, megnövelte
  ; a popup elem szélességét!
  [:div {:style {:max-width "var( --content-width-m )"}}
        [components/item-list-table selector-id
                                    {:body [item-list-body selector-id body-props]}]])

(defn- item-selector
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;  {:engine (keyword)(opt)}
  [selector-id {:keys [engine] :as body-props}]
  (let [body-props (assoc body-props :error-element [components/error-content {:error  :the-content-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :item-list :item-count 3}]
                                     :list-element  [item-list selector-id body-props])]
       (case engine :item-browser [item-browser/body selector-id body-props]
                    :item-lister  [item-lister/body  selector-id body-props]
                                  [item-lister/body  selector-id body-props])))

(defn item-selector-body
  ; A komponens további paraméterezését az engines.item-lister/body
  ; és az engines.item-browser/body komponens dokumentácójában találod!
  ;
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;  {:default-order-by (namespaced keyword)(opt)
  ;    Default: :modified-at/descending
  ;   :engine (keyword)(opt)
  ;    :item-browser, :item-lister
  ;    Default: :item-lister
  ;   :list-item-element (component or symbol)}
  ;
  ; @usage
  ;  [item-selector-body :my-selector {...}]
  ;
  ; @usage
  ;  (defn my-list-item-element [selector-id selector-props item-dex item] ...)
  ;  [item-selector-body :my-selector {:list-item-element #'my-list-item-element}]
  [selector-id body-props]
  (let [body-props (item-selector.prototypes/body-props-prototype selector-id body-props)]
       [item-selector selector-id body-props]))
