
(ns site.xgo.api
  #?(:clj  (:require [site.xgo.pages.api]
                     [site.xgo.transfer]))
  #?(:cljs (:require [site.xgo.pages.api]
                     [site.xgo.utils.url]
                     [site.xgo.wrapper.views :as wrapper])))

#?(:cljs (def wrapper wrapper/view))
