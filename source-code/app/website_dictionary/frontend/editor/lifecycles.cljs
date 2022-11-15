
(ns app.website-dictionary.frontend.editor.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :library_books
                                                            :icon-color "#8655b1"
                                                            :label      :website-dictionary
                                                            :on-click   [:x.router/go-to! "/@app-home/website-dictionary"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :library_books
                                                             :icon-color "#8655b1"
                                                             :label      :website-dictionary
                                                             :on-click   [:x.router/go-to! "/@app-home/website-dictionary"]
                                                             :vertical-weight 3}]]}})
