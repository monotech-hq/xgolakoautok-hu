
(ns app.website-content.frontend.editor.lifecycles
    (:require [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :website
                                                            :icon       :wysiwyg
                                                            :icon-color "#8655b1"
                                                            :label      :website-content
                                                            :on-click   [:router/go-to! "/@app-home/website-content"]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group      :website
                                                             :icon       :wysiwyg
                                                             :icon-color "#8655b1"
                                                             :label      :website-content
                                                             :on-click   [:router/go-to! "/@app-home/website-content"]
                                                             :vertical-weight 1}]]}})
