
(ns app.common.frontend.item-selector.views
    (:require [app.common.frontend.item-lister.views :as item-lister.views]
              [elements.api                          :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn all-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  ;
  ; @usage
  ;  [all-item-selected-button :my-selector {...}]
  [_ {:keys []}]
  [elements/toggle ::all-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :check_box
                                             :icon-family :material-icons-outlined}]}])

(defn no-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  ;
  ; @usage
  ;  [no-item-selected-button :my-selector {...}]
  [_ {:keys []}]
  [elements/toggle ::no-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :check_box_outline_blank
                                             :icon-family :material-icons-outlined}]}])

(defn some-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  ;
  ; @usage
  ;  [some-item-selected-button :my-selector {...}]
  [_ {:keys [on-s]}]
  [elements/toggle ::some-item-selected-button
                   {:disabled? false
                    :indent    {:horizontal :xxs :right :xs}
                    :content [elements/icon {:size :s
                                             :icon :indeterminate_check_box
                                             :icon-family :material-icons-outlined}]}])

(defn handle-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {}
  ;
  ; @usage
  ;  [handle-selection-button :my-selector {...}]
  [selector-id {:keys [all-downloaded-item-selected? any-downloaded-item-selected?] :as footer-props}]
  (cond all-downloaded-item-selected? [all-item-selected-button  selector-id footer-props]
        any-downloaded-item-selected? [some-item-selected-button selector-id footer-props]
        :return                       [no-item-selected-button   selector-id footer-props]))


(defn discard-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ;  {:on-discard-selection (metamorphic-event)
  ;   :selected-item-count (integer)}
  ;
  ; @usage
  ;  [discard-selection-button :my-selector {...}]
  [_ {:keys [on-discard-selection selected-item-count]}]
  [elements/button ::discard-selection-button
                   {:disabled?     (< selected-item-count 1)
                    :font-size     :xs
                    :icon          :close
                    :icon-position :right
                    :indent        {:horizontal :xxs :vertical :xs}
                    :on-click      on-discard-selection
                    :label {:content :n-items-selected :replacements [selected-item-count]}}])

(defn item-selector-footer
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

(defn search-items-field
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :search-field-placeholder (metamorphic-content)(opt)
  ;   :search-keys (keywords in vector)}
  ;
  ; @usage
  ;  [search-items-field :my-selector {...}]
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

(defn order-by-icon-button
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :order-by-options (namespaced keywords in vector)}
  ;
  ; @usage
  ;  [order-by-icon-button :my-selector {...}]
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
  [elements/row ::item-selector-control-bar
                {:content [:<> [search-items-field   selector-id bar-props]
                               [order-by-icon-button selector-id bar-props]]}])

;; -- Ghost-view components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-selector-ghost-element
  ; @param (keyword) selector-id
  ; @param (map) element-props
  ;
  ; @usage
  ;  [item-selector-ghost-element :my-selector {...}]
  [selector-id view-props]
  [item-lister.views/item-lister-ghost-element selector-id view-props])
